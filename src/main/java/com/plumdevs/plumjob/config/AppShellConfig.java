package com.plumdevs.plumjob.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;

public class AppShellConfig implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "icons/favicon.png", "192x192");
        settings.addLink("shortcut icon", "icons/favicon.png");
    }
}