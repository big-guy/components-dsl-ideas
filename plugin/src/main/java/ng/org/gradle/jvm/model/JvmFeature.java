package ng.org.gradle.jvm.model;

import ng.org.gradle.java.model.JvmDependencies;
import ng.org.gradle.software.model.Feature;
import org.gradle.api.Action;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Nested;

public interface JvmFeature extends Feature {
    ListProperty<Integer> getTargetJdks();

    @Nested
    JvmDependencies getDependencies();

    default void dependencies(Action<? super JvmDependencies> action) {
        action.execute(getDependencies());
    }

    ExtensiblePolymorphicDomainObjectContainer<JvmSourceSet> getSources();
    ExtensiblePolymorphicDomainObjectContainer<JvmTarget> getTargets();
    ExtensiblePolymorphicDomainObjectContainer<JvmVariant> getVariants();
}
