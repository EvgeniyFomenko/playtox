package org.example;

import lombok.extern.log4j.Log4j2;
import org.example.pojo.Account;
import org.example.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
@Log4j2
public class App {
    static Random random = new Random();
    static AccountService accountService = new AccountService(new AtomicInteger(0));

    public static void main(String[] args) throws InterruptedException {
        List<Account> accountList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            accountList.add(Account.builder().id(Integer.toString(random.nextInt(100))).money(10000).build());
        }


        int resStart = 0;
        for (Account account : accountList) {
            resStart += account.getMoney();
        }

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                while (accountService.getCountTransaction().get() < 30) {
                    int firstAcc = random.nextInt(accountList.size());
                    int secondAcc = random.nextInt(accountList.size());
                    if (firstAcc == secondAcc) {
                        firstAcc = random.nextInt(accountList.size());
                    }
                    accountService.transferMoneyFromFirstToSecondAccounts(accountList.get(firstAcc), accountList.get(secondAcc), random.nextInt(10000));
                    try {
                        int timeSleep = random.nextInt(2000 - 1000 + 1) + 1000;
                        log.info(String.format("Thread name = %s Sleep on time = %d", Thread.currentThread().getName(), timeSleep));
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int resEnd = 0;
        for (Account account : accountList) {
            resEnd += account.getMoney();
        }
        log.info(String.format("start = %d and end = %d", resStart, resEnd));
    }


}
