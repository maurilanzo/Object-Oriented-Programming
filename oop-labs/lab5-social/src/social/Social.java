package social;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Facade class for the social network system.
 * 
 */
public class Social {

  private final PersonRepository personRepository = new PersonRepository();
  private final GroupRepository groupRepository = new GroupRepository();
  private final PostRepository postRepository = new PostRepository();
  
  /**
   * Creates a new account for a person
   * 
   * @param code    nickname of the account
   * @param name    first name
   * @param surname last name
   * @throws PersonExistsException in case of duplicate code
   */
  public void addPerson(String code, String name, String surname) throws PersonExistsException {
    if (personRepository.findById(code).isPresent()){    // check if db already contains the code
        throw new PersonExistsException();
    }
    Person person = new Person(code, name, surname);    // create the person as a POJO
    personRepository.save(person);                      // save it to db
  }

  /**
   * Retrieves information about the person given their account code.
   * The info consists in name and surname of the person, in order, separated by
   * blanks.
   * 
   * @param code account code
   * @return the information of the person
   * @throws NoSuchCodeException if a person with that code does not exist
   */
  public String getPerson(String code) throws NoSuchCodeException {
    Optional<Person> p = personRepository.findById(code);
    if (p.isPresent()){    // check if db already contains the code
      Person person=p.get();  
      return String.format("%s %s %s", person.getCode(),person.getName(),person.getSurname());
    }
    else throw new NoSuchCodeException();
  }

  /**
   * Define a friendship relationship between two persons given their codes.
   * <p>
   * Friendship is bidirectional: if person A is adding as friend person B, that means
   * that person B automatically adds as friend person A.
   * 
   * @param codePerson1 first person code
   * @param codePerson2 second person code
   * @throws NoSuchCodeException in case either code does not exist
   */
  public void addFriendship(String codePerson1, String codePerson2)
      throws NoSuchCodeException {
        Optional<Person> p1 = personRepository.findById(codePerson1);
        Optional<Person> p2 = personRepository.findById(codePerson2);
        if (p1.isPresent() && p2.isPresent()){    // check if db already contains the code
          Person person1=p1.get();  
          Person person2=p2.get(); 

          person1.addFriend(person2);
          person2.addFriend(person1);
          personRepository.update(person2);
          personRepository.update(person1);
        }
          
          else throw new NoSuchCodeException();


    // TO BE IMPLEMENTED
  }

  /**
   * Retrieve the collection of their friends given the code of a person.
   *
   * @param codePerson code of the person
   * @return the list of person codes
   * @throws NoSuchCodeException in case the code does not exist
   */
  public Collection<String> listOfFriends(String codePerson) throws NoSuchCodeException {
      Optional<Person> p = personRepository.findById(codePerson);
      if (p.isPresent()){    // check if db already contains the code
        Person person=p.get();  
        return person.getFriends();
      }
      else throw new NoSuchCodeException();
  }

  /**
   * Creates a new group with the given name
   * 
   * @param groupName name of the group
   * @throws GroupExistsException if a group with given name does not exist
   */
  public void addGroup(String groupName) throws GroupExistsException {
    // TO BE IMPLEMENTED
    Optional<Group> g = groupRepository.findById(groupName);
    if (g.isPresent()){    // check if db already contains the name
      throw new GroupExistsException();
    }
    Group group = new Group(groupName);
    groupRepository.save(group);
    
  }

  /**
   * Deletes the group with the given name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if a group with given name does not exist
   */
  public void deleteGroup(String groupName) throws NoSuchCodeException {
    // TO BE IMPLEMENTED
    Optional<Group> g = groupRepository.findById(groupName);
    if (!g.isPresent()){   
      throw new NoSuchCodeException();
    }
    groupRepository.delete(g.get());
    
  }

  /**
   * Modifies the group name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if the original group name does not exist
   * @throws GroupExistsException if the target group name already exist
   */
  public void updateGroupName(String groupName, String newName) throws NoSuchCodeException, GroupExistsException {
    // TO BE IMPLEMENTED
    Optional<Group> g = groupRepository.findById(groupName);
    if (!g.isPresent()){   
      throw new NoSuchCodeException();
    }
    if (groupRepository.findById(newName).isPresent()){
      throw new GroupExistsException();
    }

  

    g.get().setGroup_name(newName);
    groupRepository.update(g.get());

  }

  /**
   * Retrieves the list of groups.
   * 
   * @return the collection of group names
   */
  public Collection<String> listOfGroups() {
    return groupRepository.findAll().stream().map(g->g.getGroup_name()).collect(Collectors.toList()); // TO BE IMPLEMENTED
  }

  /**
   * Add a person to a group
   * 
   * @param codePerson person code
   * @param groupName  name of the group
   * @throws NoSuchCodeException in case the code or group name do not exist
   */
  public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
    // TO BE IMPLEMENTED
    Optional<Person> p = personRepository.findById(codePerson);
    Optional<Group> g = groupRepository.findById(groupName);
    if (!p.isPresent() || !g.isPresent()){    // check if db already contains the code
      throw new NoSuchCodeException();
    }
    g.get().addMember(p.get());
    groupRepository.update(g.get());
    personRepository.update(p.get());  

  }

  /**
   * Retrieves the list of people on a group
   * 
   * @param groupName name of the group
   * @return collection of person codes
   */
  public Collection<String> listOfPeopleInGroup(String groupName) {
    Optional<Group> g = groupRepository.findById(groupName);
    if (!g.isPresent()){   
      return null;
    }
    Collection<String> col = g.get().getMembers().stream().map(p->p.getCode()).collect(Collectors.toList());
    return col;
  }

  /**
   * Retrieves the code of the person having the largest
   * group of friends
   * 
   * @return the code of the person
   */
  public String personWithLargestNumberOfFriends() {
    return personRepository.findAll().stream()
        .max(Comparator.comparingInt(p -> p.getFriends().size()))
        .map(Person::getCode)
        .orElse(null); // oppure lancia un'eccezione, se necessario
}


  /**
   * Find the name of group with the largest number of members
   * 
   * @return the name of the group
   */
  public String largestGroup() {
     return groupRepository.findAll().stream()
        .max(Comparator.comparingInt(p -> p.getMembers().size()))
        .map(Group::getGroup_name)
        .orElse(null); // TO BE IMPLEMENTED
  }

  /**
   * Find the code of the person that is member of
   * the largest number of groups
   * 
   * @return the code of the person
   */
  public String personInLargestNumberOfGroups() {
    return personRepository.findAll().stream()
        .max(Comparator.comparingInt(p -> p.getGroups().size()))
        .map(Person::getCode)
        .orElse(null);
  }

  // R5

  /**
   * add a new post by a given account
   * 
   * @param authorCode the id of the post author
   * @param text   the content of the post
   * @return a unique id of the post
   */
  private long postCounter = 0;
  public String post(String authorCode, String text) {
      postCounter++;
      // Convert the counter to a base36 alphanumeric string
      String postId = Long.toString(postCounter, 36).toUpperCase();
      Post post = new Post(postId, text, authorCode, System.currentTimeMillis(), personRepository.findById(authorCode).orElse(null));
      postRepository.save(post);
      //personRepository.findById(authorCode).ifPresent(person -> person.addPost(post));
      return postId;
  }

  /**
   * retrieves the content of the given post
   * 
   * @param pid    the id of the post
   * @return the content of the post
   */
  public String getPostContent(String pid) {
    if (postRepository.findById(pid).isPresent()) return postRepository.findById(pid).get().getText();
    return null;
  }

  /**
   * retrieves the timestamp of the given post
   * 
   * @param pid    the id of the post
   * @return the timestamp of the post
   */
  public long getTimestamp(String pid) {
    if (postRepository.findById(pid).isPresent()) return postRepository.findById(pid).get().getTimestamp();
    return -1;
  }

  /**
   * returns the list of post of a given author paginated
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts id
   */
  public List<String> getPaginatedUserPosts(String author, int pageNo, int pageLength) {
    List<Post> posts = personRepository.findById(author).get().getPosts().stream().sorted(Comparator.comparing(Post::getTimestamp).reversed()).collect(Collectors.toList());
    int i1 = (pageNo - 1) * pageLength;
    int i2 = Math.min(i1 + pageLength, posts.size());
    if (i1 >= posts.size()) return Collections.emptyList();

    return posts.subList(i1, i2).stream().map(Post::getId).collect(Collectors.toList());
  }

  /**
   * returns the paginated list of post of friends.
   * The returned list contains the author and the id of a post separated by ":"
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts key elements
   */
  public List<String> getPaginatedFriendPosts(String author, int pageNo, int pageLength) {
    int i1 = (pageNo - 1) * pageLength;
    int i2 = (i1 + pageLength);

    Optional <Person> p= personRepository.findById(author);
    if (!p.isPresent()){    // check if db already contains the code
        return null;
      }
    List<Person> friends = p.get().getFriends().stream().map(h-> personRepository.findById(h).get()).toList();
    List<String> posts = friends.stream()
      .flatMap(person-> person.getPosts()
              .stream()
              .sorted(Comparator.comparing(Post::getTimestamp)
              .reversed())
              .map(post->person.getName() + ":" +post.getId()))
      .toList()
      .subList(i1, i2);

    return posts; // TO BE IMPLEMENTED
  }

}