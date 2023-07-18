package ng.org.gradle.java;

import ng.org.gradle.java.model.JavaApi;
import ng.org.gradle.java.model.JavaDocs;
import ng.org.gradle.java.model.JavaSourceSet;
import ng.org.gradle.java.model.JavaSources;
import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.Model;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.internal.tasks.compile.JavaCompileExecutableUtils;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.internal.JvmPluginsHelper;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.gradle.jvm.toolchain.JavaToolchainService;
import org.gradle.jvm.toolchain.JavaToolchainSpec;

public abstract class JavaLanguageBasePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("ng.org.gradle.jvm-ecosystem");

        Model model = project.getExtensions().getByType(Model.class);

        // Rules that apply to every component
        model.getNgComponents().withType(Component.class).configureEach(component -> {
            component.getFeatures().withType(JvmFeature.class).configureEach(feature -> {
                // all features can have a JavaSourceSet
                feature.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);
                feature.getTargets().configureEach(target -> {
                    // all targets can have a JavaSourceSet
                    target.getSources().registerBinding(JavaSourceSet.class, JavaSourceSet.class);

                    target.getVariants().registerBinding(JavaApi.class, JavaApi.class);
                    target.getVariants().registerBinding(JavaDocs.class, JavaDocs.class);
                    target.getVariants().registerBinding(JavaSources.class, JavaSources.class);

                    target.getSources().withType(JavaSourceSet.class).configureEach(sourceSet -> {
                        // TODO: Capitalize
                        project.getTasks().register("compile" + sourceSet.getName(), JavaCompile.class, task -> {
                            ConventionMapping conventionMapping = task.getConventionMapping();

                            // TODO: This should be more human-readable or have a display name derived from target + source set
                            task.setDescription("Compiles " + sourceSet.getName() + " sources for " + target.getName() + ".");
                            task.source(sourceSet.getSrcDirs());

                            // conventionMapping.map("classpath", sourceSet::getCompileClasspath);

                            // TODO: Modified from
                            //  JvmPluginsHelper.configureAnnotationProcessorPath(sourceSet, javaSource, task.getOptions(), project);
                            conventionMapping.map("annotationProcessorPath", () -> project.getObjects().fileCollection());
                            String annotationProcessorGeneratedSourcesChildPath = "generated/sources/annotationProcessor/" + sourceSet.getName() + "/" + feature.getName();
                            task.getOptions().getGeneratedSourceOutputDirectory().convention(project.getLayout().getBuildDirectory().dir(annotationProcessorGeneratedSourcesChildPath));

                            // TODO: This needs to honor Java extension and executable overrides?
                            JavaToolchainService service = project.getExtensions().getByType(JavaToolchainService.class);
                            task.getJavaCompiler().convention(service.compilerFor(spec -> {
                                spec.getLanguageVersion().convention(target.getTargetJdk().map(JavaLanguageVersion::of));
                            }));

                            String generatedHeadersDir = "generated/sources/headers/" + sourceSet.getName() + "/" + feature.getName();
                            task.getOptions().getHeaderOutputDirectory().convention(project.getLayout().getBuildDirectory().dir(generatedHeadersDir));

                            // TODO:
                            task.getModularity().getInferModulePath().convention(false);
                        });
                    });
                });

            });
        });
    }
}
