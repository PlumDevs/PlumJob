package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value="about", layout = MainLayout.class) //the view will appear on localhost:8080/about, and use layout from MainLayout besides about own unique components
public class AboutView extends VerticalLayout {
}
