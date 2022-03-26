package ucm.appmenus.ui.filtros;

import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeMenuCallback;
import org.mapsforge.map.rendertheme.XmlThemeResourceProvider;

import java.io.InputStream;

/**
 * Enumeration of all internal rendering themes.
 */
public enum InternalRenderTheme implements XmlRenderTheme {

    DEFAULT("/assets/mapsforge/default.xml"),
    OSMARENDER("/assets/mapsforge/osmarender.xml");

    private final String path;

    InternalRenderTheme(String path) {
        this.path = path;
    }

    @Override
    public XmlRenderThemeMenuCallback getMenuCallback() {
        return null;
    }

    /**
     * @return the prefix for all relative resource paths.
     */
    @Override
    public String getRelativePathPrefix() {
        return "/assets/";
    }

    @Override
    public InputStream getRenderThemeAsStream() {
        return getClass().getResourceAsStream(this.path);
    }

    @Override
    public XmlThemeResourceProvider getResourceProvider() {
        return null;
    }

    @Override
    public void setMenuCallback(XmlRenderThemeMenuCallback menuCallback) {
    }

    @Override
    public void setResourceProvider(XmlThemeResourceProvider resourceProvider) {
    }
}
