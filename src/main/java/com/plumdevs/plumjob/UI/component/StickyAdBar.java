package com.plumdevs.plumjob.UI.component;

import com.plumdevs.plumjob.service.TagService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.*;

public class StickyAdBar extends Div {

    private static class AdImage {
        String imagePath;
        String linkUrl;

        AdImage(String imagePath, String linkUrl) {
            this.imagePath = imagePath;
            this.linkUrl = linkUrl;
        }
    }

    private final Map<String, List<AdImage>> adsByIndustry = new HashMap<>();

    private int currentAdIndex = 0;

    public StickyAdBar(TagService tagService, AuthenticationContext authContext) {
        setStyle();

        adsByIndustry.put("Software Development", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/praca"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/praca"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/praca"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/software%20developer;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/software%20developer;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/software%20developer;kw")
        ));

        adsByIndustry.put("Data Science", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/big-data-science;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/big-data-science;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/big-data-science;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/data%20scientist;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/data%20scientist;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/data%20scientist;kw")
        ));

        adsByIndustry.put("Cybersecurity", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/security;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/security;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/security;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/cyber%20security%20specialist;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/cyber%20security%20specialist;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/cyber%20security%20specialist;kw")
        ));

        adsByIndustry.put("Product Management", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/product-management;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/product-management;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/product-management;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/product%20manager;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/product%20manager;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/product%20manager;kw")
        ));
        adsByIndustry.put("UI/UX Design", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/ux-ui;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/ux-ui;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/ux-ui;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/ui%20ux%20designer;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/ui%20ux%20designer;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/ui%20ux%20designer;kw")
        ));
        adsByIndustry.put("Cloud Engineering", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/praca?kw=cloud+computing"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/praca?kw=cloud+computing"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/praca?kw=cloud+computing"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/cloud%20engineer;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/cloud%20engineer;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/cloud%20engineer;kw")
        ));
        adsByIndustry.put("Project Management", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/project-management;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/project-management;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/project-management;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/project%20manager;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/project%20manager;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/project%20manager;kw")
        ));
        adsByIndustry.put("QA / Testing", Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/filtry/qa-testing;sp"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/filtry/qa-testing;sp"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/filtry/qa-testing;sp"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/qa%20tester;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/qa%20tester;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/qa%20tester;kw")
        ));

        List<AdImage> defaultAds = Arrays.asList(
                new AdImage("/images/phe_protocol_ad_1.png", "https://theprotocol.it/praca"),
                new AdImage("/images/phe_protocol_ad_2.png", "https://theprotocol.it/praca"),
                new AdImage("/images/phe_protocol_ad_3.png", "https://theprotocol.it/praca"),
                new AdImage("/images/pracuj_pl_ad_1.png", "https://www.pracuj.pl/praca/software%20developer;kw"),
                new AdImage("/images/pracuj_pl_ad_2.png", "https://www.pracuj.pl/praca/software%20developer;kw"),
                new AdImage("/images/pracuj_pl_ad_3.png", "https://www.pracuj.pl/praca/software%20developer;kw")
        );

        String username = authContext.getPrincipalName().orElse(null);
        List<AdImage> userAds = defaultAds;
        if (username != null) {
            String industry = tagService.getTagValueForType(username, "industry");
            if (industry != null && adsByIndustry.containsKey(industry)) {
                userAds = adsByIndustry.get(industry);
            }
        }

        currentAdIndex = new Random().nextInt(userAds.size());
        AdImage firstAd = userAds.get(currentAdIndex);
        Image adImage = new Image(firstAd.imagePath, "Advertisement");
        adImage.setWidth("601px");
        adImage.setHeight("100px");

        Anchor adLink = new Anchor(firstAd.linkUrl, adImage);
        adLink.setTarget("_blank");

        Button closeButton = new Button("✖", e -> setVisible(false));
        stylizeCloseButton(closeButton);

        add(adLink, closeButton);

        List<AdImage> finalUserAds = userAds;
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int randomIndex = new Random().nextInt(finalUserAds.size());
                AdImage nextAd = finalUserAds.get(randomIndex);
                getUI().ifPresent(ui ->
                        ui.access(() -> {
                            adImage.setSrc(nextAd.imagePath);
                            adLink.setHref(nextAd.linkUrl);
                            ui.push();
                        })
                );
            }
        }, 20000, 20000);

    }

    private void setStyle() {
        setWidth("600px");
        getStyle()
                .set("position", "fixed")
                .set("bottom", "40px")
                .set("left", "50%")
                .set("transform", "translateX(-50%)")
                .set("padding", "0")
                .set("z-index", "9999")
                .set("box-sizing", "border-box")
                .set("text-align", "center");
    }

    private void stylizeCloseButton(Button closeButton) {
        closeButton.getStyle()
                .set("position", "absolute")
                .set("top", "0")
                .set("right", "0")
                .set("background", "#ccc")
                .set("border-radius", "0")
                .set("border", "none")
                .set("font-size", "14px")
                .set("width", "14px")
                .set("height", "14px")
                .set("line-height", "14px")
                .set("text-align", "center")
                .set("cursor", "pointer")
                .set("color", "#444")
                .set("padding", "0")
                .set("margin", "0")
                .set("box-sizing", "border-box");
    }
}
