package org.example;

import com.github.javafaker.Faker;
import entities.Customer;
import entities.Order;
import entities.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {

        Supplier<Product> productSupplier = getProductSupplier();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            productList.add(productSupplier.get());
        }

        Supplier<Customer> customerSupplier = getCustomerSupplier();
        List<Customer> customerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customerList.add(customerSupplier.get());
        }

        Supplier<Order> orderSupplier = getOrderSupplier();
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderList.add(orderSupplier.get());
        }


//        es1
        System.out.println("es1");

        Map<Customer, List<Order>> orderByCustomer = orderList.stream().collect(Collectors.groupingBy(Order::getCustomer));

        orderByCustomer.forEach((customer, customerOrderList) -> System.out.println("Costumer: " + customer.getName() + " - " + "order " +customerOrderList));

        System.out.println("----------------------------------------------------");

//        es2
        System.out.println("es2");

        Map<Customer, Double> totalOrderByCustomer = orderList.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(order ->
                        order.getProducts().stream().mapToDouble(Product::getPrice).sum())));

        totalOrderByCustomer.forEach((customer, totalOrder)-> System.out.println("Customer: " + customer.getName() + " - " + "total of order: " + totalOrder + " $"));

        System.out.println("----------------------------------------------------");

//        
    }
    public static Supplier<Product> getProductSupplier() {
        Random rdm = new Random();
        Faker faker = new Faker();

        return () -> {
            long rdmId = rdm.nextLong(10000, 100000);

            String rmdName = faker.commerce().productName();

            int rdmCategory = rdm.nextInt(0, 5);

            double rdmPrice = rdm.nextDouble(10.00, 200.00);


            List<String> categoryList = List.of("Books", "Boys", "Baby", "Cars", "Guns");

            return new Product(rdmId, rmdName, categoryList.get(rdmCategory), rdmPrice);
        };
    }
    public static Supplier<Customer> getCustomerSupplier() {
        Random rmd = new Random();
        Faker faker = new Faker();

        return () -> {
            long rmdId = rmd.nextLong(10000, 100000);

            int rmdTier = rmd.nextInt(1, 7);

            String rmdName = faker.pokemon().name();



            return new Customer(rmdId, rmdName, rmdTier);
        };
    }

    public static Supplier<Order> getOrderSupplier() {
        Random rdm = new Random();

        return () -> {
            long rdmId = rdm.nextLong(10000, 100000);

            int rdmStatus = rdm.nextInt(0, 2);

//            dates
            LocalDate startDate = LocalDate.parse("2021-01-01");

            int rdmProduct = rdm.nextInt(1, 5);
//            products
            List<Product> rdmProductList = new ArrayList<>();
            for (int i = 0; i < rdmProduct; i++) {
                rdmProductList.add(getProductSupplier().get());
            }

            Customer rdmCustomer = getCustomerSupplier().get();

            LocalDate orderDate = startDate.plusDays(rdm.nextInt(1, 500));
            LocalDate deliveryDate = orderDate.plusDays(rdm.nextInt(14));

            List<String> statusList = List.of("Shipped", "Error");

            return new Order(rdmId, statusList.get(rdmStatus), orderDate, deliveryDate, rdmProductList, rdmCustomer);
        };
    }
}
