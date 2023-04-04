package ng.org.gradle.software.model;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;

import javax.inject.Inject;

public abstract class DefaultModel implements Model {
    @Inject
    public DefaultModel(ExtensiblePolymorphicDomainObjectContainer<Component> components) {

    }
}
