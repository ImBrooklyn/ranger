package uk.org.brooklyn.ranger.utils;

import org.junit.jupiter.api.Test;
import uk.org.brooklyn.ranger.model.NodeStat;

/**
 * @author ImBrooklyn
 * @since 23/03/2024
 */
public class BeanFormatterTest {
    @Test
    public void testFormat() {
        NodeStat nodeStat = NodeStat.builder()
                .build();
        String output = BeanFormatter.format(nodeStat);
        System.out.println(output);
    }
}
