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

        //Check that User Inputs are not empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "titleEmpty", "Title can not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "problem", "problemEmpty", "Problem can not be empty");

        //Check that User Inputs are not null
        if(Objects.isNull(ticket.getTitle())){
            errors.rejectValue("title", "noTitle", "There has to be a title");
        }

        if(Objects.isNull(ticket.getProblem())){
            errors.rejectValue("problem", "noProblem", "There has to be a problem");
        }

        if(Objects.isNull(ticket.getCategory())){
            errors.rejectValue("category", "noCategory", "There has to be a category selected");
        }

        //Check that User Inputs offer value and are not to long
        if((ticket.getTitle().length()>50) || ticket.getTitle().length()<5){
            errors.rejectValue("title", "wrongTitleSize", "Title should be between 5 and 50 Characters long");
        }

        if((ticket.getProblem().length()>500) || ticket.getProblem().length()<10){
            errors.rejectValue("problem", "wrongProblemSize", "Problem should be between 10 and 500 Characters long");
        }
    }
}
