package ng.org.gradle.software;

import javax.inject.Inject;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class ModelRendererRegistry {
    public interface Renderer {
        void render(Renderer out, Object object);
    }
    private final Map<Class, Renderer> renderers = new HashMap<>();

    @Inject
    public ModelRendererRegistry() {

    }

    private static class PassThruRenderer implements Renderer {
        @Override
        public void render(Renderer out, Object object) {
            out.render(out, object);
        }
    }

    private static final Renderer PASSTHRU = new PassThruRenderer();

    public <T> void renderTo(PrintStream printStream, Class<T> clazz, T o) {
        renderers.getOrDefault(clazz, PASSTHRU).render((out, object) -> printStream.println(object), o);
    }

    public void addRenderer(Class clazz, Renderer renderer) {
        renderers.put(clazz, renderer);
    }
}
