package com.khudim.dao.docs;

/**
 * Created by Beaver.
 */

import com.khudim.dao.HibernateTestConfiguration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.khudim.dao.docs.DocumentsFields.PRICE;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {HibernateTestConfiguration.class})
public class DocumentsRepositoryTest {

    @Autowired
    private
    DocumentsRepository documentsRepository;

    @Test
    public void shouldSaveDocument() {
        Assert.assertEquals(0, documentsRepository.getAllDocumentsCount());
        Documents document = new Documents();
        document.setGuid("1");
        document.setPrice(2d);
        document.setUrl("url");
        document.setRegion("spb");
        document.setContent("content");
        document.setCreationDate(1941L);
        documentsRepository.updateDocument(document);
        Assert.assertEquals(1, documentsRepository.getAllDocumentsCount());
        Assert.assertEquals(document, documentsRepository.getByKey("1"));
    }

    @Test
    public void shouldGetAllRegions() {
        Assert.assertEquals(0, documentsRepository.getAllDocumentsCount());
        String[] regions = {"spb", "Moskva", "Kiev", "Che", "Ekb"};
        documents.forEach(doc -> documentsRepository.updateDocument(doc));
        Assert.assertEquals(5, documentsRepository.getAllDocumentsCount());
        Assert.assertEquals(regions.length, documentsRepository.getAllRegions().size());
    }


    @Test
    public void shouldGetAllDocumentsWithFilter() {
        Assert.assertEquals(0, documentsRepository.getAllDocumentsCount());
        documents.forEach(doc -> documentsRepository.updateDocument(doc));
        SimpleExpression expression = Restrictions.ge(PRICE, 3d);
        List<Documents> documents = documentsRepository.getAllDocuments(Collections.singletonList(expression),0,10, Order.asc(PRICE));
        Assert.assertEquals(3, documents.size());
    }

    private List<Documents> documents = new ArrayList<>();

    {
        Documents document = new Documents();
        document.setGuid("1");
        document.setPrice(1d);
        document.setUrl("url");
        document.setRegion("spb");
        document.setContent("content");
        document.setCreationDate(1911L);
        Documents document2 = new Documents();
        document2.setGuid("2");
        document2.setPrice(2d);
        document2.setUrl("url");
        document2.setRegion("Moskva");
        document2.setContent("content");
        document2.setCreationDate(1921L);
        Documents document3 = new Documents();
        document3.setGuid("3");
        document3.setPrice(3d);
        document3.setUrl("url");
        document3.setRegion("Kiev");
        document3.setContent("content");
        document3.setCreationDate(1931L);
        Documents document4 = new Documents();
        document4.setGuid("4");
        document4.setPrice(4d);
        document4.setUrl("url");
        document4.setRegion("Che");
        document4.setContent("content");
        document4.setCreationDate(1941L);
        Documents document5 = new Documents();
        document5.setGuid("5");
        document5.setPrice(5d);
        document5.setUrl("url");
        document5.setRegion("Ekb");
        document5.setContent("content");
        document5.setCreationDate(1951L);
        documents.add(document);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);
        documents.add(document5);
    }
}