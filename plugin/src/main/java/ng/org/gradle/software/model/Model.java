package ng.org.gradle.software.model;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;

public interface Model {
    ExtensiblePolymorphicDomainObjectContainer<Component> getNgComponents();
}
