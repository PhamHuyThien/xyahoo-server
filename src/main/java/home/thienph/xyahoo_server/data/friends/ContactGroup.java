package home.thienph.xyahoo_server.data.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ContactGroup {
    public List<ContactInfo> contacts;
    private String groupName;
    private int groupStatus; // (0=collapsed, 1=expanded), 90%

    public ContactGroup(String contactName) {
        this.groupName = contactName;
        this.contacts = new ArrayList<>();
    }
}
