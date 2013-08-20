package gov.pnnl.stucco.collectors;

import java.io.File;
import java.util.Map;

/**
 * $OPEN_SOURCE_DISCLAIMER$
 */

public class CollectorFactory {
    
    // simple factory approach, will probably want to redo this as we include more collector types
    public static Collector makeCollector(String collectorType, String src, Map<String, Object> configData) {
        Collector aCollector = null;
        if(collectorType.equals("WEB")) {
            CollectorWebPageImpl webCollector = new CollectorWebPageImpl(src, configData);
            aCollector = webCollector;
        } else if(collectorType.equals("FILE")) {
            CollectorFileImpl fileCollector = new CollectorFileImpl(new File(src));
            aCollector = fileCollector;
        } else if(collectorType.equals("DIRECTORY")) {
            CollectorDirectoryImpl dirCollector = new CollectorDirectoryImpl(new File(src));
            aCollector = dirCollector;
        } else if (collectorType.equals("RSS")) {
            // TODO: 
        }
        
        return aCollector;
    }

}