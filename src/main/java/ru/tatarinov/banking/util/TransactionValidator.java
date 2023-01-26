package ru.tatarinov.banking.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transaction;

@Component
public class TransactionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Transaction.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Transaction transaction = (Transaction) target;
        if (transaction.getAmount() < 0.01)
            errors.rejectValue("amount", "", "Сумма должна быть больше 0");
        if (transaction.getDestination() == null)
            errors.rejectValue("destination", "", "Получатель не найден");
        Card card = transaction.getSource();
        if (card.getBalance() < transaction.getAmount())
            errors.rejectValue("source", "", "Недостаточно средств");
    }
}
