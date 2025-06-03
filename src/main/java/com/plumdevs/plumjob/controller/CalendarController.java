package com.plumdevs.plumjob.controller;

import com.plumdevs.plumjob.UI.component.Calendar;
import com.plumdevs.plumjob.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vaadin.flow.spring.security.AuthenticationContext;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CalendarController {

    private EventRepository eventRepository;
    private AuthenticationContext authContext;

    @Autowired
    public CalendarController(EventRepository eventRepository, AuthenticationContext authContext) {
        this.eventRepository = eventRepository;
        this.authContext = authContext;
    }

    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public String calendar(Model model) {
        Calendar calendar = new Calendar(eventRepository, authContext);
        model.addAttribute("calendar", calendar);
        return "calendar";
    }

}