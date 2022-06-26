package de.hohenheim.ticketcricket.model.validation;

import de.hohenheim.ticketcricket.model.entity.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class TicketValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Ticket.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ticket ticket = (Ticket) target;

        if(Objects.isNull(ticket.getCategory())){
            errors.rejectValue("category", "noCategory", "Bitte wählen Sie eine Kategorie aus.");
        }

        //Check that User Inputs offer value and are not to long
        if((ticket.getTitle().length()>50) || ticket.getTitle().length()<5){
            errors.rejectValue("title", "wrongTitleSize", "Bitte geben Sie einen Titel mit einer Länge von 5 bis 50 Zeichen an.");
        }

        if((ticket.getProblem().length()>250) || ticket.getProblem().length()<10){
            errors.rejectValue("problem", "wrongProblemSize", "Bitte geben Sie eine Problemlänge von 10 bis 250 Zeichen an.");
        }
    }
}
