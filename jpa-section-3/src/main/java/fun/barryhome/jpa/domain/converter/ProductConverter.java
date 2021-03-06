package fun.barryhome.jpa.domain.converter;

import fun.barryhome.jpa.domain.product.AbstractProduct;
import fun.barryhome.jpa.domain.product.Book;
import fun.barryhome.jpa.domain.product.Food;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created on 2020/3/18 0018 22:53
 *
 * @author Administrator
 * Description:
 */
//@Converter(autoApply=true)
public class ProductConverter implements AttributeConverter<AbstractProduct, String> {

    @Override
    public String convertToDatabaseColumn(AbstractProduct attribute) {
        if (attribute instanceof Food) {
            return ((Food) attribute).getFoodCode();
        } else if (attribute instanceof Book) {
            return ((Book) attribute).getBookIsbn();
        }

        return "";
    }


    @Override
    public AbstractProduct convertToEntityAttribute(String dbData) {
        if (dbData.startsWith("B")) {
            return Book.builder().bookIsbn(dbData).build();
        } else {
            return Food.builder().foodCode(dbData).build();
        }

    }
}
