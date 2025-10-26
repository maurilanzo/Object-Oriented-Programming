package social;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
class Person {
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable( 
    name="friendship",
    joinColumns = @JoinColumn(name = "code"),
    inverseJoinColumns = @JoinColumn(name = "friendcode")
  )
  private Set<Person> friends = new HashSet<>();

@ManyToMany (fetch = FetchType.EAGER)//Person is the owning side and the set groups store the groups where the person participate
@JoinTable(
    name = "group_membership",
    joinColumns = @JoinColumn(name = "person_code"),
    inverseJoinColumns = @JoinColumn(name = "group_name")
)
private Set<Group> groups = new HashSet<>();

@OneToMany(mappedBy = "author",fetch = FetchType.EAGER)
private Set<Post> posts = new HashSet<>();

@Id
private String code;
private String name;
private String surname;

public Person() {
  // default constructor is needed by JPA
}

Person(String code, String name, String surname) {
  this.code = code;
  this.name = name;
  this.surname = surname;
}


String getCode() {
  return code;
}

String getName() {
  return name;
}

String getSurname() {
  return surname;
}

public void addFriend(Person p){
  friends.add(p);
}

public Collection<String> getFriends(){
  return friends.stream().map(f->f.getCode()).toList();
}

public Set<Group> getGroups(){
  return groups;
}
public void addPost(Post post){
  posts.add(post);
}

public Set<Post> getPosts() {
return posts;
}

//....
}
