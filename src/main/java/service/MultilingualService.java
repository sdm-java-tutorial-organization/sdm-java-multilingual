package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultilingualService {

    private static final Logger logger = LoggerFactory.getLogger(MultilingualService.class);

    private static MultilingualService instance = new MultilingualService();

    private MultilingualService() {
        logger.debug("create MultilingualService");
    }

    public static MultilingualService getInstance() {
        return instance;
    }

    /*public getBodyFromProperties() {

    }*/

}
