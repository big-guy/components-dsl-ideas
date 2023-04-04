package ng.org.gradle.software.model;

import ng.org.gradle.software.ModelRendererRegistry;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.tasks.Nested;

public interface Model {
    @Nested
    ModelRendererRegistry getRenderers();
    ExtensiblePolymorphicDomainObjectContainer<Component> getComponents();
}
