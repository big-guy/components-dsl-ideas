package ng.org.gradle.jvm.model;

import org.gradle.api.Named;
import org.gradle.api.file.ConfigurableFileCollection;

import java.io.File;
import java.util.Iterator;

public interface JvmSourceSet extends Named, Iterable<File> {
    ConfigurableFileCollection getSrcDirs();

    default void srcDir(Object srcDir) {
        getSrcDirs().from(srcDir);
    }

    // TODO: How do we implement this properly?
    @Override
    default Iterator<File> iterator() {
        return getSrcDirs().iterator();
    }
}
