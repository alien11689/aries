package org.apache.aries.subsystem.core.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.aries.application.Content;
import org.apache.aries.application.management.BundleInfo;
import org.apache.aries.subsystem.SubsystemConstants;
import org.apache.aries.subsystem.SubsystemException;
import org.apache.aries.subsystem.core.obr.BundleInfoImpl;
import org.apache.aries.subsystem.core.obr.ContentImpl;
import org.apache.aries.subsystem.core.obr.Manve2Repository;
import org.apache.aries.subsystem.core.obr.RepositoryDescriptorGenerator;
import org.apache.aries.subsystem.spi.Resource;
import org.apache.aries.subsystem.spi.ResourceResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.obr.Repository;
import org.osgi.service.obr.RepositoryAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class ResourceResolverImpl implements ResourceResolver {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResourceResolverImpl.class);

    final private BundleContext context;
    private RepositoryAdmin repositoryAdmin;
    private static boolean generated = false;
    private String obrPath;

    public ResourceResolverImpl(BundleContext context) {
        this.context = context;
    }

    public ResourceResolverImpl(BundleContext context, String obrPath) {
        this.context = context;
        this.obrPath = obrPath;
    }
    
    public  void generateOBR() {
        if (generated) {
            return;
        }
        synchronized(this) {
            if (obrPath == null) {
                // set to a default obr file which is local m2 repo
                String file = System.getProperty("user.home") + "/.m2/repository/";
                if (new File(file).exists()) {
                    obrPath = file;
                }
    
            }
    
            File rootFile = new File(obrPath);
            if (!rootFile.exists() || !rootFile.isDirectory()) {
                throw new IllegalArgumentException("obr path " + obrPath
                        + " is not valid");
            }
    
            Manve2Repository repo = new Manve2Repository(rootFile);
    
            SortedSet<String> ss = repo.listFiles();
            Set<BundleInfo> infos = new HashSet<BundleInfo>();
    
            for (String s : ss) {
                BundleInfo info = new BundleInfoImpl(s);
                infos.add(info);
            }
    
            Document doc;
            try {
                doc = RepositoryDescriptorGenerator.generateRepositoryDescriptor(
                        "Subsystem Repository description", infos);
                FileOutputStream fout = new FileOutputStream(obrPath
                        + "repository.xml");
    
                TransformerFactory.newInstance().newTransformer().transform(
                        new DOMSource(doc), new StreamResult(fout));
    
                fout.close();
    
                TransformerFactory.newInstance().newTransformer().transform(
                        new DOMSource(doc), new StreamResult(System.out));
            } catch (Exception e) {
                LOGGER.error("Exception occurred when generate obr", e);
                e.printStackTrace();
            }
    
            registerOBR();
            
            generated = true;

        }

    }

    private void registerOBR() {
        // set repositoryAdmin
        ServiceReference ref = context
                .getServiceReference(RepositoryAdmin.class.getName());
        
        if (ref != null) {
            this.repositoryAdmin = (RepositoryAdmin) context.getService(ref);
    
            try {
                this.repositoryAdmin.addRepository(new File(obrPath
                        + "repository.xml").toURI().toURL());
            } catch (Exception e) {
                LOGGER.warn("Exception occurred when register obr", e);
                e.printStackTrace();
            }
    
            this.context.ungetService(ref);
        } else {
            LOGGER.error("Unable to register OBR as RepositoryAdmin service is not available");
        }

    }

    /**
     * the format of resource is like bundlesymbolicname;version=1.0.0, for example com.ibm.ws.eba.example.blog.api;version=1.0.0,
     */
    public Resource find(String resource) throws SubsystemException {
        generateOBR();
        
        Content content = new ContentImpl(resource);
        
        String symbolicName = content.getContentName();
        // this version could possibly be a range
        String version = content.getVersion().toString();
        StringBuilder filterString = new StringBuilder();
        filterString.append("(&(name" + "=" + symbolicName + "))");
        filterString.append("(version" + "=" + version + "))");

        //org.osgi.service.obr.Resource[] res = this.repositoryAdmin.discoverResources(filterString.toString());
        Repository[] repos = this.repositoryAdmin.listRepositories();
        org.osgi.service.obr.Resource res = null;
        for (Repository repo : repos) {
            org.osgi.service.obr.Resource[] resources = repo.getResources();
            for (int i = 0; i < resources.length; i++) {
                if (resources[i].getSymbolicName().equals(symbolicName)) {
                    if (resources[i].getVersion().compareTo(new Version(version)) == 0) {
                        res = resources[i];
                    }
                }
            }
        }
        if (res == null) {
            throw new SubsystemException("unable to find the resource " + resource);
        }
        
        Map props = res.getProperties();
        

        Object type = props.get(SubsystemConstants.RESOURCE_TYPE_ATTRIBUTE);

        return new ResourceImpl(symbolicName, res.getVersion(), type == null ? SubsystemConstants.RESOURCE_TYPE_BUNDLE : (String)type, res.getURL().toExternalForm() , props);
    }

    public List<Resource> resolve(List<Resource> subsystemContent,
            List<Resource> subsystemResources) throws SubsystemException {
        generateOBR();
        
        

        return subsystemResources;
    }

}
