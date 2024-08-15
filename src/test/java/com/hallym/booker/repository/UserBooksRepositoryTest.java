package com.hallym.booker.repository;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserBooksRepositoryTest {

    @Autowired
    private UserBooksRepository userBooksRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BookDetailsRepository bookDetailsRepository;

    @PersistenceContext
    private EntityManager em; // EntityManager 주입

    @Test
    public void testFindAllByProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        List<UserBooks> userBooksList = userBooksRepository.findAllByProfile(profiles);

        //then
        assertThat(userBooksList).isNotEmpty();
        assertThat(userBooksList.get(0).getProfile()).isEqualTo(profiles);
    }

    @Test
    public void testFindReadStatusByProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 1, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        List<Integer> readStatusList = userBooksRepository.findReadStatusByProfile(profiles);

        //then
        assertThat(readStatusList).isNotEmpty();
        assertThat(readStatusList.get(0)).isEqualTo(1);
    }

    @Test
    public void testFindSaleStatusByProfile() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 1);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        List<Integer> saleStatusList = userBooksRepository.findSaleStatusByProfile(profiles);

        //then
        assertThat(saleStatusList).isNotEmpty();
        assertThat(saleStatusList.get(0)).isEqualTo(1);
    }

    @Test
    public void testFindAllProfilesByIsbn() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        List<Profile> profilesList = userBooksRepository.findAllProfilesByIsbn("isbn");

        //then
        assertThat(profilesList).isNotEmpty();
        assertThat(profilesList.get(0)).isEqualTo(profiles);
    }

    @Test
    public void testFindByProfileUidAndIsbn() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        UserBooks foundUserBook = userBooksRepository.findByProfileUidAndIsbn(profiles, "isbn");

        //then
        assertThat(foundUserBook).isNotNull();
        assertThat(foundUserBook.getProfile()).isEqualTo(profiles);
        assertThat(foundUserBook.getBookDetails().getIsbn()).isEqualTo("isbn");
    }

    @Test
    public void testFindByIsbnAndSalesstate() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 1);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        List<Profile> profilesList = userBooksRepository.findByIsbnAndSalesstate("isbn");

        //then
        assertThat(profilesList).isNotEmpty();
        assertThat(profilesList.get(0)).isEqualTo(profiles);
    }

    @Test
    public void testDeleteUserBooks() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);

        //when
        userBooksRepository.delete(userBooks);
        Optional<UserBooks> deletedUserBook = userBooksRepository.findById(userBooks.getBookUid());

        //then
        assertThat(deletedUserBook).isEmpty();

        // log deletion confirmation
        // logger.info("Deleted UserBooks with id {}", savedUserBook.getId());

    }

    @Test
    public void findWithProfileListTest() {
        //given
        Date now = new Date();
        Login login = Login.create("id", "pw", "email", now);
        Login logins = loginRepository.save(login);
        Login login2 = Login.create("id2", "pw2", "email", now);
        Login logins2 = loginRepository.save(login2);

        Profile profile = Profile.create(logins, "nickname", "userimageUrl", "userimageName", "usermessage");
        Profile profiles = profileRepository.save(profile);
        Profile profile2 = Profile.create(logins2, "nickname2", "userimageUrl2", "userimageName2", "usermessage2");
        Profile profiles2 = profileRepository.save(profile2);

        BookDetails bookDetails = BookDetails.create("isbn", "bookTitle", "author", "publisher", "coverImageUrl");
        BookDetails bookDetail1 = bookDetailsRepository.save(bookDetails);
        BookDetails bookDetails2 = BookDetails.create("isbn2", "bookTitle2", "author2", "publisher2", "coverImageUrl2");
        BookDetails bookDetail2 = bookDetailsRepository.save(bookDetails2);

        UserBooks userBook = UserBooks.create(profiles, bookDetail1, 0, 0);
        UserBooks userBooks = userBooksRepository.save(userBook);
        UserBooks userBook2 = UserBooks.create(profiles, bookDetail2, 0, 0);
        UserBooks userBooks2 = userBooksRepository.save(userBook2);
        UserBooks userBook3 = UserBooks.create(profiles2, bookDetail1, 0, 0);
        UserBooks userBooks3 = userBooksRepository.save(userBook);
        
        //then
        List<UserBooks> withProfileList = userBooksRepository.findWithProfileList(profiles.getProfileUid());

        //then
        Assertions.assertThat(withProfileList).extracting(UserBooks::getProfile).extracting(Profile::getNickname).contains("nickname");
    }

}
