package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.DiagramLink;
import com.plumdevs.plumjob.repository.DiagramRepository;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
        this.links = links;
    }

    public List<DiagramLink> getLinksForUser(String userId) { //issue todo
        return diagramRepository.callDiagramLinkProcedure(userId);
    }
    public String convertToJs() {
        StringBuilder js = new StringBuilder(); //for more efficient run than String
        js.append("[");
        int numOfLinks = links.size();

        for (int i = 0; i < numOfLinks; i++) {
            DiagramLink currentLink = links.get(i);
            js.append("['");
            js.append(currentLink.getFrom());
            js.append("','");
            js.append(currentLink.getTo());
            js.append("',");
            js.append(currentLink.getWeight());
            js.append("]");

            if ((i + 1) < numOfLinks) {
                js.append(",");
            }
        }

        js.append("]");
        return js.toString();
    }
}
