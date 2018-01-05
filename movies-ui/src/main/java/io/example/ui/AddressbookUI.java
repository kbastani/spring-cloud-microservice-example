package io.example.ui;


import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import io.example.clients.UserClient;
import io.example.models.User;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

@SpringUI
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI {

    private TextField filter = new TextField();
    private Grid<User> contactList = new Grid<>(User.class);
    private Button newContact = new Button("New contact");
    private ContactForm contactForm = new ContactForm();
    private final UserClient userClient;
    private final SpringViewProvider viewProvider;

    @Autowired
    public AddressbookUI(UserClient userClient, SpringViewProvider viewProvider) {
        this.userClient = userClient;
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        newContact.addClickListener(e -> contactForm.edit(new User()));
        filter.setCaption("Filter contacts...");
        filter.addValueChangeListener(e -> refreshContacts(e.getValue()));
        contactList.setColumnOrder("id", "firstName", "lastName", "email");
        contactList.removeColumn("birthDate");
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.addSelectionListener(e -> contactForm.edit(contactList.getSelectedItems()
                .stream().findFirst().orElse(null)));
        refreshContacts();
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newContact);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, contactList);
        left.setSizeFull();
        contactList.setSizeFull();
        left.setExpandRatio(contactList, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, contactForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        // Split and allow resizing
        setContent(mainLayout);
    }

    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {
        contactList.setDataProvider(DataProvider.ofCollection(userClient.findAll().getContent()));
        contactForm.setVisible(false);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }

    @WebListener
    public static class SpringContextLoaderListener extends ContextLoaderListener {
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public Grid<User> getContactList() {
        return contactList;
    }
}
