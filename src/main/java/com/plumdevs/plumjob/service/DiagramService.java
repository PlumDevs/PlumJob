package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.entity.DiagramLink;
import com.plumdevs.plumjob.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DiagramService {

    @Autowired
    private DiagramRepository diagramRepository;

    List<DiagramLink> links;

    public DiagramService() {
        //links = null;
    }

    public DiagramService(DiagramRepository diagramRepository) {
        this.diagramRepository = diagramRepository;
        links = null;
    }

    public DiagramService(List<DiagramLink> links, DiagramRepository diagramRepository) {
        this.diagramRepository = diagramRepository;
        this.links = links;
    }

    public DiagramService(List<DiagramLink> links) {
        this.links = new ArrayList<>(links);
    }

    public List<DiagramLink> getLinksForUser(String userId) {
        return diagramRepository.callDiagramLinkProcedure(userId);
    }
    public String convertToJs() {

        StringBuilder js = new StringBuilder();
        js.append("[");

        int numOfLinks = links.size();

        for (int i = 0; i < numOfLinks; i++) {
            DiagramLink link = links.get(i);

            String from = (link.getFrom() != null) ? link.getFrom().replace("'", "\\'") : "";
            String to = (link.getTo() != null) ? link.getTo().replace("'", "\\'") : "";
            int weight = link.getWeight();

            js.append("['")
                    .append(from)
                    .append("','")
                    .append(to)
                    .append("',")
                    .append(weight)
                    .append("]");

            if (i < numOfLinks - 1) {
                js.append(",");
            }
        }

        js.append("]");
        return js.toString();
    }

}
