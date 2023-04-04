package ng.org.gradle.jvm.model;

import org.gradle.api.Named;

public interface JvmSourceSet extends Named {
    default void srcDir(Object srcDir) {

    }
}
