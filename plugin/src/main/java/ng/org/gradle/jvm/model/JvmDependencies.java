package ng.org.gradle.jvm.model;

import org.gradle.api.provider.ListProperty;

import java.util.Objects;

public interface JvmDependencies {
    ListProperty<String> getApiDependencies();
    ListProperty<String> getImplementationDependencies();

    default void api(Object dep) {
        getApiDependencies().add(Objects.toString(dep));
    }

    default void implementation(Object dep) {
        getImplementationDependencies().add(Objects.toString(dep));
    }
}
