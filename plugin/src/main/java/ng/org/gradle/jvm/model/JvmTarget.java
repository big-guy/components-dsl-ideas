package ng.org.gradle.jvm.model;

import ng.org.gradle.software.model.Target;
import org.gradle.api.Action;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

public interface JvmTarget extends Target {
    // TODO: should be a proper type
    Property<Integer> getTargetJdk();

    ExtensiblePolymorphicDomainObjectContainer<JvmSourceSet> getSources();

    @Nested
    JvmDependencies getDependencies();

    default void dependencies(Action<? super JvmDependencies> action) {
        action.execute(getDependencies());
    }

    ExtensiblePolymorphicDomainObjectContainer<JvmVariant> getVariants();

    default String nameOf(String name) {
        return name + getName();
    }

    default void compilation(Action<? super JvmCompilation> action) {
        getVariants().withType(JvmCompilation.class).configureEach(action);
    }
}
