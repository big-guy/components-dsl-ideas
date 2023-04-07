package ng.org.gradle.software.model;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Named;

public interface Target extends Named {
    ExtensiblePolymorphicDomainObjectContainer<? extends Variant> getVariants();
}
