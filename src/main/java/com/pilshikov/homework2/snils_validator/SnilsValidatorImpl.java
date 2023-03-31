package com.pilshikov.homework2.snils_validator;

public class SnilsValidatorImpl implements SnilsValidator{

    @Override
    public boolean validate(String snils) {
        // Проверяем что в строке только цифры и строка == 11 символов
        if (!onlyNumbersInString(snils) || (snils.length() != 11) )
            return false;

        // Получаем из строки контрольное число
        int enteredControlNumber = Integer.parseInt(snils.substring(9, 11));

        // Находим контрольное число с помощью алгоритма и сравниваем с переданным
        int n = multiplyNumbers(snils.substring(0,9));
        int foundControlNumber = findControlNumber(n);

        return enteredControlNumber == foundControlNumber;
    }

    // Возвращает true, если в переданной строке только цифры
    public boolean onlyNumbersInString(String number) {
        for (int i = 0; i < number.length(); i++) {
            char charAtIndex = number.charAt(i);
            if (!Character.isDigit(charAtIndex))
                return false;
        }
        return true;
    }


    // Возвращает сумму произведений цифр на коэффициенты (9..1)
    public int multiplyNumbers(String number) {
        int summ = 0;
        for (int i = 0; i < number.length(); i++) {
            char charAtIndex = number.charAt(i);
            summ += Character.getNumericValue(charAtIndex) * (number.length() - i);
        }
        return summ;
    }
    
    // Возвращает контрольное число для number
    public int findControlNumber(int number) {
        if (number < 100) {
            return number;
        } else if (number > 100) {
            int remainder = number % 101;
            if (remainder == 100) {
                return 0;
            } else {
                return remainder;
            }
        }
        else {
            return 0;
        }
    }


}
