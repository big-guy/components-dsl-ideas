package ng.org.gradle.software.model;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Named;

public interface Component extends Named {
    ExtensiblePolymorphicDomainObjectContainer<Feature> getFeatures();
}
