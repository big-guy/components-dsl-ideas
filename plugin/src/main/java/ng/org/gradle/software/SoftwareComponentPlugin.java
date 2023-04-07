package ng.org.gradle.software;

import ng.org.gradle.java.model.JavaApi;
import ng.org.gradle.java.model.JavaDocs;
import ng.org.gradle.java.model.JavaSources;
import ng.org.gradle.jvm.model.JvmFeature;
import ng.org.gradle.jvm.model.JvmRuntime;
import ng.org.gradle.jvm.model.JvmTarget;
import ng.org.gradle.software.model.Component;
import ng.org.gradle.software.model.LibraryComponent;
import ng.org.gradle.software.model.Model;
import ng.org.gradle.software.model.Variant;
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

public abstract class SoftwareComponentPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Model model = project.getExtensions().create(Model.class, "ng", Model.class);
        ExtensiblePolymorphicDomainObjectContainer<Component> components = model.getNgComponents();

        //
        components.registerBinding(LibraryComponent.class, LibraryComponent.class);

        // TODO: force the realization of everything
        /*project.afterEvaluate(p -> {
            model.getNgComponents().all(component -> {
                component.getFeatures().all(feature -> {
                    feature.getTargets().all(target -> {
                        target.getVariants().all(variant -> {

                        });
                    });
                });
            });
        });*/

        project.getTasks().register("printModel", t -> {
            t.doLast(task -> {
                IndentedPrintStream out = new IndentedPrintStream(System.out);
                model.getNgComponents().forEach(component -> {
                    out.println("Component: " + component.getName() + " {");
                    out.indent();
                    component.getFeatures().forEach(feature -> {
                        out.println("Feature: " + feature.getName() + " {");
                        out.indent();
                        // TODO: implement a proper renderer so this can be kept generic
                        if (feature instanceof JvmFeature) {
                            out.println("JDKs: " + ((JvmFeature) feature).getTargetJdks().map(jdks -> jdks.toString()).getOrElse("(unset)"));
                            ((JvmFeature) feature).getSources().forEach(source -> {
                                out.println("Common source: " + source.getName());
                                out.indent();
                                source.getSrcDirs().getFiles().forEach(srcDir -> {
                                    out.println(srcDir);
                                });
                                out.dedent();
                            });

                            out.println("Dependencies: ");
                            out.indent();
                            List<String> apiDependencies = ((JvmFeature) feature).getDependencies().getApiDependencies().get();
                            if (!apiDependencies.isEmpty()) {
                                apiDependencies.forEach(dep -> {
                                    out.println("api " + dep);
                                });
                            } else {
                                out.println("No api dependencies.");
                            }
                            List<String> implementationDependencies = ((JvmFeature) feature).getDependencies().getImplementationDependencies().get();
                            if (!implementationDependencies.isEmpty()) {
                                implementationDependencies.forEach(dep -> {
                                    out.println("implementation " + dep);
                                });
                            } else {
                                out.println("No implementation dependencies.");
                            }
                            out.dedent();
                        }
                        feature.getTargets().forEach(target -> {
                            out.println("Target: " + target.getName() + " {");
                            out.indent();
                            if (target instanceof JvmTarget) {
                                out.println("JDK: " + ((JvmTarget) target).getTargetJdk().map(jdks -> jdks.toString()).getOrElse("(unset)"));

                                ((JvmTarget) target).getSources().forEach(source -> {
                                    out.println("Source: " + source.getName());
                                    out.indent();
                                    source.getSrcDirs().getFiles().forEach(srcDir -> {
                                        out.println(srcDir);
                                    });
                                    out.dedent();
                                });

                                out.println("Dependencies: ");
                                out.indent();
                                List<String> apiDependencies = ((JvmTarget) target).getDependencies().getApiDependencies().get();
                                if (!apiDependencies.isEmpty()) {
                                    apiDependencies.forEach(dep -> {
                                        out.println("api " + dep);
                                    });
                                } else {
                                    out.println("No api dependencies.");
                                }
                                List<String> implementationDependencies = ((JvmTarget) target).getDependencies().getImplementationDependencies().get();
                                if (!implementationDependencies.isEmpty()) {
                                    implementationDependencies.forEach(dep -> {
                                        out.println("implementation " + dep);
                                    });
                                } else {
                                    out.println("No implementation dependencies.");
                                }
                                out.dedent();

                                ((JvmTarget) target).getVariants().forEach(variant -> {
                                    renderVariant(out, variant);
                                });
                            }
                            out.dedent();
                            out.println("}");
                        });
                        out.dedent();
                        out.println("}");
                    });
                    out.dedent();
                    out.println("}");
                });
            });
        });
    }

    private void renderVariant(IndentedPrintStream out, Variant variant) {
        out.println("Variant: " + variant.getName() + " {");
        out.indent();
        if (variant instanceof JavaApi) {

        } else if (variant instanceof JvmRuntime) {

        } else if (variant instanceof JavaSources) {

        } else if (variant instanceof JavaDocs) {

        } else {
            out.println(variant);
        }
        out.dedent();
        out.println("}");
    }
}
