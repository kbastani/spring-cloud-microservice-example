package service.ui;


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
import service.clients.MovieClient;
import service.clients.RatingClient;
import service.clients.UserClient;
import service.models.Genre;
import service.models.Movie;
import service.models.Product;
import service.models.User;

import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringUI(path = "/users")
@Title("Users")
@Theme("valo")
public class UserUI extends UI {

    private static final long serialVersionUID = -3540851800967573466L;

    TextField filter = new TextField();
    Grid items = new Grid();
    Grid ratings = new Grid();

    @Autowired
    MovieClient movieClient;

    @Autowired
    UserClient userClient;

    @Autowired
    RatingClient ratingClient;

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        filter.setInputPrompt("Search movies...");
        items.setContainerDataSource(new BeanItemContainer<>(User.class));
        items.setColumnOrder("id", "age", "gender", "occupation", "zipcode");
        ratings.setContainerDataSource(new BeanItemContainer<>(Movie.class));
        ratings.setColumnOrder("id", "title", "genres", "url");

        ratings.setVisible(false);

        filter.addShortcutListener(new ShortcutListener("Press enter to submit...", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                refreshUsers(filter.getValue());
            }
        });

        items.setSelectionMode(Grid.SelectionMode.SINGLE);
        items.addSelectionListener(e -> {
            populateMovies(((User) items.getSelectedRow()).getId());
            ratings.setVisible(true);
        });


        ratings.getColumn("genres").setRenderer(new HtmlRenderer(),
                new Converter<String, Genre[]>() {
                    @Override
                    public Genre[] convertToModel(String value, Class<? extends Genre[]> targetType, Locale locale)
                            throws ConversionException {
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
        ratings.getColumn("url").setRenderer(new HtmlRenderer(),
                new Converter<String, String>() {
                    @Override
                    public String convertToModel(String value,
                                                 Class<? extends String> targetType, Locale locale)
                            throws ConversionException {
                        return "not implemented";
                    }

                    @Override
                    public String convertToPresentation(String value,
                                                        Class<? extends String> targetType, Locale locale)
                            throws ConversionException {
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
        refreshUsers();
    }

    /**
     * Populates the grid of movie records that the user has reviewed
     *
     * @param userId is the ID record of the user
     */
    private void populateMovies(Long userId) {

        // Get the movies that this user has rated
        List<Product> productList = ratingClient.findProductsByUser(userId.toString())
                .getContent()
                .stream()
                .collect(Collectors.toList());

        // Translate those records to movie records
        ratings.setContainerDataSource(new BeanItemContainer<>(Movie.class,
                movieClient.findByIds(productList.stream().map(a -> a.getKnownId()).collect(Collectors.joining(","))).getContent()));
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setWidth("100%");
        filter.setWidth("100%");
        ratings.setHeight("100%");
        ratings.setWidth("100%");
        actions.setExpandRatio(filter, 1);
        VerticalLayout left = new VerticalLayout(actions, items);
        left.setSizeFull();
        items.setSizeFull();
        left.setExpandRatio(items, 1);
        HorizontalLayout mainLayout = new HorizontalLayout(left, ratings);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);
        setContent(mainLayout);
    }

    void refreshUsers() {
        refreshUsers(filter.getValue());
    }

    private void refreshUsers(String stringFilter) {
        if (!Objects.equals(stringFilter.trim(), "")) {
            items.setContainerDataSource(new BeanItemContainer<>(User.class, userClient.findAll().getContent()));

        }
    }

    @WebServlet(urlPatterns = "/users")
    @VaadinServletConfiguration(ui = UserUI.class, productionMode = false)
    public static class UsersServlet extends VaadinServlet {

    }
}
