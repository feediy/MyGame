package com.fung.server.content.config.readconfig;

import com.fung.server.content.config.npc.NpcOption;
import com.fung.server.excel2class.AbstractJsonModelListManager;
import org.springframework.stereotype.Component;

/**
 * @author skytrc@163.com
 * @date 2020/6/17 10:56
 */
@Component
public class ReadNpcOption extends AbstractJsonModelListManager<NpcOption> {
    public ReadNpcOption() {
        super("npcOption", "npc");
    }
}
