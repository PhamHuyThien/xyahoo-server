package home.thienph.xyahoo_server.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CommandFactory {
    @Autowired
    @Qualifier("xYahooCommand")
    Command xyahooCommand;

    public Command getCommand(int typeId) {
        return switch (typeId) {
            //
            default -> xyahooCommand;
        };
    }
}
