package com.khudim.dao.docs;

/**
 * Created by Beaver.
 */

import com.khudim.dao.HibernateTestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

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
        String[] regions = {"spb", "moska", "kiev"};
        Documents document1 = new Documents();
        document1.setGuid("1");
        document1.setRegion(regions[0]);
        documentsRepository.updateDocument(document1);
        Documents document2 = new Documents();
        document2.setGuid("2");
        document2.setRegion(regions[1]);
        documentsRepository.updateDocument(document2);
        Documents document3 = new Documents();
        document3.setGuid("3");
        document3.setRegion(regions[2]);
        documentsRepository.updateDocument(document3);
        Assert.assertEquals(3, documentsRepository.getAllDocumentsCount());
        Assert.assertArrayEquals(regions,documentsRepository.getAllRegions().toArray());
    }
}