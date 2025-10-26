package social;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Groups")
public class Group {
    @Id
    private String group_name;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER) //group is the inverse side, and store the set of all the persons in the group
    private Set<Person> members = new HashSet<>();

    public Group() {}

    public Group(String name) {
        this.group_name = name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Collection<Person> getMembers (){
        return members;
    }

    public void addMember(Person p) {
        members.add(p);
        p.getGroups().add(this);
    }
    



}
