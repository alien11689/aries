/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aries.subsystem.core.obr.felix;

import org.apache.felix.bundlerepository.Capability;
import org.apache.felix.bundlerepository.Requirement;
import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleRevision;

public class OsgiRequirementAdapter implements Requirement {
	private final org.osgi.framework.wiring.Requirement requirement;
	
	public OsgiRequirementAdapter(org.osgi.framework.wiring.Requirement requirement) {
		if (requirement == null)
			throw new NullPointerException("Missing required parameter: requirement");
		this.requirement = requirement;
	}

	public String getComment() {
		return null;
	}

	public String getFilter() {
		return requirement.getDirectives().get(Constants.FILTER_DIRECTIVE);
	}

	public String getName() {
		String namespace = requirement.getNamespace();
		if (namespace.equals(BundleRevision.BUNDLE_NAMESPACE))
			return Capability.BUNDLE;
		if (namespace.equals(BundleRevision.HOST_NAMESPACE))
			return Capability.FRAGMENT;
		if (namespace.equals(BundleRevision.PACKAGE_NAMESPACE))
			return Capability.PACKAGE;
		return namespace;
	}

	public boolean isExtend() {
		return false;
	}

	public boolean isMultiple() {
		return false;
	}

	public boolean isOptional() {
		String resolution = requirement.getDirectives().get(Constants.RESOLUTION_DIRECTIVE);
		return Constants.RESOLUTION_OPTIONAL.equals(resolution);
	}

	public boolean isSatisfied(Capability capability) {
		return requirement.matches(new FelixCapabilityAdapter(capability, requirement.getResource()));
	}

}
