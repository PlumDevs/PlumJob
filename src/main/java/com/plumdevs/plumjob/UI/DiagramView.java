package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.DiagramLink;
import com.plumdevs.plumjob.repository.DiagramRepository;
import com.plumdevs.plumjob.service.DiagramService;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.text.html.HTML;
import java.util.List;

@PermitAll
@PageTitle("Plum Job - Diagram")
@Route(value="diagram", layout = MainLayout.class)
public class DiagramView extends VerticalLayout {


    public DiagramView(DiagramRepository diagramRepository, UserService userService) {
        HorizontalLayout titleBar = new HorizontalLayout();
        H2 title = new H2("Your job search summarized");

        Button backToArchive = new Button("Back");
        backToArchive.addClassName("light-button");
        backToArchive.addClickListener(buttonClickEvent -> getUI().ifPresent(ui ->
                ui.navigate("archive")));

        titleBar.add(title, backToArchive);
        add(titleBar);

        List<DiagramLink> links = diagramRepository.callDiagramLinkProcedure(userService.getUsername());

        DiagramService diagramService = new DiagramService(links);

        if (links.isEmpty()) {
            add(new Paragraph("No recruitment history in archive"));
            return;
        }

        String js = diagramService.convertToJs(); //array of links converted to javascript ready to inject below

        Div chartDiv = new Div();
        chartDiv.setId("sankey_basic");
        chartDiv.setWidth("600px");
        chartDiv.setHeight("400px");

        add(chartDiv);

        UI.getCurrent().getPage().addJavaScript("https://www.gstatic.com/charts/loader.js");

        System.out.print(js);

        UI.getCurrent().getPage().executeJs(
                "google.charts.load('current', {packages:['sankey']});" +
                        "google.charts.setOnLoadCallback(drawChart);" +
                        "function drawChart() {" +
                        "  var data = new google.visualization.DataTable();" +
                        "  data.addColumn('string', 'from');" +
                        "  data.addColumn('string', 'to');" +
                        "  data.addColumn('number', 'amount');" +
                        "data.addRows(" + js + ");" +
                        "  var colors = ['#730D3F', '#941f58', '#c43f80', '#e065a1', '#d799ae'];\n" +
                        "\n" +
                        "\n" +
                        "          // Sets chart options.\n" +
                        "          var options = {\n" +
                        "          width: 1000,\n" +
                        "          sankey: {\n" +
                        "            node: {\n" +
                        "              colors: colors,\n" +
                        "              nodePadding: 120\n" +
                        "            },\n" +
                        "            link: {\n" +
                        "                color: {\n" +
                        "                    fill: colors[4],\n" +
                        "                    strokeWidth: 1\n" +
                        "                },\n" +
                        "            },\n" +
                        "          }\n" +
                        "        };" +
                        "  var chart = new google.visualization.Sankey(document.getElementById('sankey_basic'));" +
                        "  chart.draw(data, options);" +
                        "};"
        );
    }

}
