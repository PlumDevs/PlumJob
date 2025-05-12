package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.stream.Stream;

public class RecruitmentItemDetails extends FormLayout {

    private final TextField descField = new TextField("Description");
    private final TextField startDateField = new TextField("Start date");
    private final TextField jobLink = new TextField("Job posting link");
    public RecruitmentItemDetails() {
        Stream.of(descField, startDateField, jobLink).forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });

        setResponsiveSteps(new ResponsiveStep("0", 3));
        setColspan(descField, 3);
        setColspan(startDateField, 3);
        setColspan(jobLink, 3);
    }

    public void setItem(RecruitmentItem item) {

        //String descText = item.getDescription();
        String descText = item.getDescription();

        if (descText == null) {
            descField.setValue("");
            return;
        }

        descField.setValue(descText);
        //startDateField.setValue(item.getStartDate().toString());
        startDateField.setValue("");
        jobLink.setValue(""); //temp
    }

}
