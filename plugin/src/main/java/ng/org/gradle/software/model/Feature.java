package ng.org.gradle.software.model;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Named;

public interface Feature extends Named {

    ExtensiblePolymorphicDomainObjectContainer<? extends Target> getTargets();
}
