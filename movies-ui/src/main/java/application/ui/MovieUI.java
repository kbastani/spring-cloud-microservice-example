package application.ui;


import application.clients.MovieClient;
import application.models.Genre;
import application.models.Movie;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@SpringUI(path = "")
@Title("Movies")
@Theme("valo")
public class MovieUI extends UI {

    private static final long serialVersionUID = -3540851800967573466L;

    TextField filter = new TextField();
    Grid movieList = new Grid();

    @Autowired
    MovieClient movieClient;

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        filter.setInputPrompt("Search movies...");
        movieList.setContainerDataSource(new BeanItemContainer<>(Movie.class));
        movieList.setColumnOrder("id", "title", "genres", "url");

        filter.addShortcutListener(new ShortcutListener("Press enter to submit...", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                refreshMovies(filter.getValue());
            }
        });

        movieList.getColumn("genres").setRenderer(new HtmlRenderer(),
                new Converter<String, Genre[]>() {
                    @Override
                    public Genre[] convertToModel(String value, Class<? extends Genre[]> targetType, Locale locale)
                            throws Converter.ConversionException {
                        return null;
                    }

                    @Override
                    public String convertToPresentation(Genre[] value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                        StringBuilder sb = new StringBuilder();
                        Arrays.asList(value).forEach(a -> sb.append("<span class=\"v-label v-widget v-panel\" style=\"margin: -4px 0.5em 0px; color: gray; padding: 0.25em; display: inline-block; font-size: 0.75em; font-weight: 300;\">").append(a.getName()).append("</span>"));
                        return sb.toString();
                    }

                    @Override
                    public Class<Genre[]> getModelType() {
                        return Genre[].class;
                    }

                    @Override
                    public Class<String> getPresentationType() {
                        return String.class;
                    }
                });

        // Set the custom link renderer for movies
        movieList.getColumn("url").setRenderer(new HtmlRenderer(),
                new Converter<String, String>() {
                    @Override
                    public String convertToModel(String value,
                                                 Class<? extends String> targetType, Locale locale)
                            throws Converter.ConversionException {
                        return "not implemented";
                    }

                    @Override
                    public String convertToPresentation(String value,
                                                        Class<? extends String> targetType, Locale locale)
                            throws Converter.ConversionException {
                        return "<a href='" +
                                value + "' target='_blank'>" + value + "</a>";
                    }

                    @Override
                    public Class<String> getModelType() {
                        return String.class;
                    }

                    @Override
                    public Class<String> getPresentationType() {
                        return String.class;
                    }
                });
        refreshMovies();
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);
        VerticalLayout left = new VerticalLayout(actions, movieList);
        left.setSizeFull();
        movieList.setSizeFull();
        left.setExpandRatio(movieList, 1);
        HorizontalLayout mainLayout = new HorizontalLayout(left);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);
        setContent(mainLayout);
    }

    void refreshMovies() {
        refreshMovies(filter.getValue());
    }

    private void refreshMovies(String stringFilter) {
        if(!Objects.equals(stringFilter.trim(), "")) {
            movieList.setContainerDataSource(new BeanItemContainer<>(
                    Movie.class, movieClient.findByTitleContainingIgnoreCase(stringFilter).getContent()));
        }
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = MovieUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        private static final long serialVersionUID = -20648475329898675L;
    }
}
