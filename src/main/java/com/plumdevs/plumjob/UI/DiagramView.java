package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.DiagramLink;
import com.plumdevs.plumjob.repository.DiagramRepository;
import com.plumdevs.plumjob.service.DiagramService;
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


    public DiagramView(DiagramRepository diagramRepository) {
        //todo: rethink the diagram type - when it comes to job search timerframe?
        //or just take a cap always from one before last accepted offer (not including) to last accepted offer (defined job search frame), like defined boxes, whole db divided to searches, and the user can choose only from those calculated timeframes :))
        //therefore, we avoid diagrams that do not make sense or generation errors!!!

        HorizontalLayout titleBar = new HorizontalLayout();
        H2 title = new H2("Your job search summarized");
        Paragraph diagramTimeframe = new Paragraph("[2024-01-01 - 2025-06-12]");

        Button backToArchive = new Button("Back");
        backToArchive.addClassName("light-button");
        backToArchive.addClickListener(buttonClickEvent -> getUI().ifPresent(ui ->
                ui.navigate("archive")));

        titleBar.add(title, diagramTimeframe, backToArchive);
        add(titleBar);

        /*
        List<DiagramLink> links1 = List.of(
                new DiagramLink("Applications", "Interviews", 10), //temp, TODO: DB INTEGRATION
                new DiagramLink("Applications", "Rejected", 30),
                new DiagramLink("Applications", "No answer", 10),
                new DiagramLink("Interviews", "Offers", 5),
                new DiagramLink("Interviews", "Rejected", 5),
                new DiagramLink("Offers", "Accepted", 1),
                new DiagramLink("Offers", "Declined", 4)

        );
         */

        //DiagramService diagramService = new DiagramService(diagramRepository); //TODO REVIEW THE ARCHITERCTURE!

        //List<DiagramLink> links = diagramService.getLinksForUser((((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));

        List<DiagramLink> links = diagramRepository.callDiagramLinkProcedure("diagramTester1");

        DiagramService diagramService = new DiagramService(links);

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
