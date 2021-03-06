package io.smartcat.data.loader;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.smartcat.data.loader.model.Address;
import io.smartcat.data.loader.model.User;

public class SubListRuleTest {

    @Test
    public void should_set_list_property() {
        RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
        randomUserBuilder.randomFrom("username", "destroyerOfW0rldz")
                .randomSubListFrom("favoriteMovies", "Predator", "LotR").toBeBuilt(1000);

        List<User> result = randomUserBuilder.buildAll();

        Assert.assertEquals(1000, result.size());

        boolean atLeastOneEmptyList = false;
        boolean atLeastOneWithListOfSizeOne = false;
        boolean atLeastOneWithListOfSizeTwo = false;

        for (User u : result) {
            Assert.assertEquals("destroyerOfW0rldz", u.getUsername());
            if (u.getFavoriteMovies().isEmpty()) {
                atLeastOneEmptyList = true;
            } else if (u.getFavoriteMovies().size() == 1) {
                atLeastOneWithListOfSizeOne = true;

                String movie = u.getFavoriteMovies().get(0);
                boolean movieIsPredator = movie.equals("Predator");
                boolean movieIsLotR = movie.equals("LotR");
                Assert.assertTrue("movie must be either Predator or LotR, but was" + movie,
                        movieIsPredator || movieIsLotR);
            } else {
                atLeastOneWithListOfSizeTwo = true;
                Assert.assertEquals(u.getFavoriteMovies().size(), 2);
                Assert.assertTrue("both movies must be present in a list",
                        u.getFavoriteMovies().contains("Predator") && u.getFavoriteMovies().contains("LotR"));
            }
        }

        Assert.assertTrue("should be at least one with empty list.", atLeastOneEmptyList);
        Assert.assertTrue("should be at least one with list of size one.", atLeastOneWithListOfSizeOne);
        Assert.assertTrue("should be at least one with list of size two.", atLeastOneWithListOfSizeTwo);

    }

    @Test
    @Ignore
    public void should_set_list_of_nested_objects() {
        // TODO this will be implemented as part of the ussue ##37
        RandomBuilder<Address> randomAddressBuilder = new RandomBuilder<Address>(Address.class);
        randomAddressBuilder.randomFrom("city", "Isengard", "Minas Tirith")
                .randomFrom("street", "White Wizzard Boulevard", "Palantir's Square")
                .randomFromRange("houseNumber", 5L, 7L);

        RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
        randomUserBuilder.randomFrom("username", "destroyerOfW0rldz")
                .randomSubListWithBuilder("otherAddresses", randomAddressBuilder, 0, 2).toBeBuilt(1000);

        List<User> result = randomUserBuilder.buildAll();

        Assert.assertEquals(1000, result.size());

        boolean atLeastOneEmptyList = false;
        boolean atLeastOneWithListOfSizeOne = false;
        boolean atLeastOneWithListOfSizeTwo = false;

        boolean atLeastOneIsengard = false;
        boolean atLeastOneMinasTirith = false;

        boolean atLeastOneWWB = false;
        boolean atLeastOnePS = false;

        for (User u : result) {
            Assert.assertEquals("destroyerOfW0rldz", u.getUsername());
            if (u.getFavoriteMovies().isEmpty()) {
                atLeastOneEmptyList = true;
            } else if (u.getFavoriteMovies().size() == 1) {
                atLeastOneWithListOfSizeOne = true;

                String city = u.getOtherAddresses().get(0).getCity();
                boolean cityIsIsengard = city.equals("Isengard");
                boolean cityIsMinasTirith = city.equals("Minas Tirith");
                Assert.assertTrue("city must be either Isengard or Minas Tirith, but was" + city,
                        cityIsIsengard || cityIsMinasTirith);

                if (cityIsIsengard) {
                    atLeastOneIsengard = true;
                }
                if (cityIsMinasTirith) {
                    atLeastOneMinasTirith = true;
                }

                String street = u.getOtherAddresses().get(0).getStreet();
                boolean streetIsWWB = street.equals("White Wizzard Boulevard");
                boolean streetIsPS = street.equals("Palantir's Square");

                Assert.assertTrue(
                        "street must be either White Wizzard Boulevard or Palantir's Square, but was" + street,
                        streetIsWWB || streetIsPS);

                if (streetIsWWB) {
                    atLeastOneWWB = true;
                }

                if (streetIsPS) {
                    atLeastOnePS = true;
                }

                long houseNumber = u.getOtherAddresses().get(0).getHouseNumber();
                Assert.assertTrue("house number must be either 5 or 6, but was" + houseNumber,
                        houseNumber == 5 || houseNumber == 6);

            } else {
                atLeastOneWithListOfSizeTwo = true;
                Assert.assertEquals(u.getOtherAddresses().size(), 2);
            }
        }

        Assert.assertTrue("should be at least one with empty list.", atLeastOneEmptyList);
        Assert.assertTrue("should be at least one with list of size one.", atLeastOneWithListOfSizeOne);
        Assert.assertTrue("should be at least one with list of size two.", atLeastOneWithListOfSizeTwo);

        Assert.assertTrue("should be at least one with city Isengard.", atLeastOneIsengard);
        Assert.assertTrue("should be at least one with city Minas Thirith.", atLeastOneMinasTirith);

        Assert.assertTrue("should be at least one with street White Wizzard Boulevard.", atLeastOneWWB);
        Assert.assertTrue("should be at least one with street Palantir's Square.", atLeastOnePS);

    }

}
