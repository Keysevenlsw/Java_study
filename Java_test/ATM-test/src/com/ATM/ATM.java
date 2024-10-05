package com.ATM;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAccount; //记住登录的账户

    // 欢迎页面实现
    public void start(){
        while (true) {
            System.out.println("===欢迎进入ATM系统===");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("没有操作~~~");
            }
        }
    }

    // 用户开户方法实现
    private void createAccount(){
        Account ac = new Account();

        System.out.println("请输入您的账户名称：");
        String name = sc.next();
        ac.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别");
            char sex = sc.next().charAt(0);
            if (sex == 'F' || sex == 'M'){
                ac.setSex(sex);
                break;
            }
            else {
                System.out.println("您输入的性别只能是F或者M~~~");
            }
        }

        while (true) {
            System.out.println("请您输入您的密码：");
            String password = sc.next();
            System.out.println("请您再次输入密码：");
            String password2 = sc.next();
            if (password.equals(password2)){
                ac.setPassWord(password);
                break;
            }else {
                System.out.println("您两次输入的密码不一致，请重新输入~~~");
            }
        }

        System.out.println("请您输入您的取现额度：");
        double limit = sc.nextDouble();
        ac.setLimit(limit);

        //id
        String newCard = createCardId();
        ac.setCardId(newCard);

        accounts.add(ac);
        System.out.println("恭喜您，" + ac.getUserName() + "开户成功，您的卡号是：" + ac.getCardId());
    }

    // 创建卡号
    private String createCardId(){
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId += data;
            }
            Account ac = getAccountByCardId(cardId);
            if (ac == null){
                return cardId;
            }
        }
    }

    // 根据卡号查找账户
    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account ac = accounts.get(i);
            if (ac.getCardId().equals(cardId)){
                return ac;
            }
        }
        return null;
    }

    // 实现用户登录
    private void login(){
        System.out.println("==系统登录==");
        if (accounts.size() == 0){
            System.out.println("当前系统中无任何账户，请先开户！！！");
            return;
        }

        System.out.println("请您输入您的卡号：");
        String cardId = sc.next();
        Account ac = getAccountByCardId(cardId);
        if (ac == null){
            System.out.println("您输入的卡号不存在，请确认~~~");
        }else {
            while (true) {
                System.out.println("请您输入登录密码：");
                String password = sc.next();
                if (ac.getPassWord().equals(password)){
                    System.out.println("恭喜您，" + ac.getUserName() + "成功登录了，您的卡号是：" + ac.getCardId());
                    loginAccount = ac;
                    showUserCommand();
                    return;
                }else {
                    System.out.println("您输入的密码不正确，请确认~~~");
                }
            }
        }
    }

    // 登录后的操作界面
    private void showUserCommand(){
        while (true) {
            System.out.println(loginAccount.getUserName() + "您可以选择如下功能进行账户的处理=====");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、密码修改");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请输入：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassWord();
                    return;
                case 6:
                    System.out.println(loginAccount.getUserName() + "成功退出登录~~");
                    return;
                case 7:
                    if (deleteAccount()){
                        return;
                    }
                    break;
                default:
                    System.out.println("您当前选择的操作是不存在的，请确认~~");
            }
        }

    }

    private void updatePassWord() {
        System.out.println("==账户密码修改操作==");
        while (true) {
            System.out.println("请输入当前账户密码：");
            String password = sc.next();
            if (loginAccount.getPassWord().equals(password)){
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassWord = sc.next();
                    System.out.println("请再次输入密码：");
                    String newPassWord2 = sc.next();
                    if (newPassWord2.equals(newPassWord)){
                        loginAccount.setPassWord(newPassWord);
                        System.out.println("恭喜您，成功修改密码~~");
                        return;
                    }else {
                        System.out.println("您两次输入的密码不一致~~");
                    }
                }
            }else {
                System.out.println("您当前输入的密码不正确~~");
            }
        }
    }

    // 销户操作
    private boolean deleteAccount() {
        System.out.println("==进行销户操作==");
        System.out.println("请问您确认销户吗？y/n");
        String answer = sc.next();
        switch (answer){
            case "y":
                if (loginAccount.getMoney() == 0){
                    accounts.remove(loginAccount);
                    System.out.println("您好，您的账户已经注销~~");
                    return true;
                }else {
                    System.out.println("对不起，您的账户中还有钱，不允许销户~~");
                    return false;
                }
            default:
                System.out.println("好的，账户保存。");
                return false;
        }
    }

    // 转账
    private void transferMoney() {
        System.out.println("==用户转账==");

        if (accounts.size() < 2){
            System.out.println("当前系统中账户不足两个，无法转账~~");
            return;
        }

        if (loginAccount.getMoney() == 0){
            System.out.println("账户没钱，无法转账~~");
            return;
        }

        while (true) {
            System.out.println("请您输入对方卡号：");
            String cardId = sc.next();
            Account ac = getAccountByCardId(cardId);
            if (ac == null){
                System.out.println("您输入的卡号不存在~~~");
            }else {
                String name = "*" + ac.getUserName().substring(1);
                System.out.println("请您输入[" + name + "]的姓氏：");
                String preName = sc.next();
                if (ac.getUserName().startsWith(preName)){
                    while (true) {
                        System.out.println("请您输入转账给对方的金额：");
                        double money = sc.nextDouble();
                        if (money > loginAccount.getMoney()){
                            System.out.println("余额不足，最多可转：" + loginAccount.getMoney());
                        }else {
                            loginAccount.setMoney(loginAccount.getMoney() - money);
                            ac.setMoney(ac.getMoney() + money);
                            return;
                        }
                    }
                }else {
                    System.out.println("对不起，输入错误~~");
                }
            }
        }
    }

    // 取钱
    private void drawMoney() {
        System.out.println("==取钱操作==");
        if (loginAccount.getMoney() < 100){
            System.out.println("您的余额不足100元，不允许取钱~~");
            return;
        }
        while (true) {
            System.out.println("请您输入取钱金额：");
            double money = sc.nextDouble();

            if (loginAccount.getMoney() >= money){
                if (money > loginAccount.getLimit()){
                    System.out.println("您当前取款金额超过限额，您没次最多可取：" + loginAccount.getLimit());
                }else {
                    loginAccount.setMoney(loginAccount.getMoney() - money);
                    System.out.println("您取款：" + money + "成功，取款后余额为：" + loginAccount.getMoney());
                    break;
                }
            }else {
                System.out.println("余额不足。");
            }
        }
    }

    // 存钱
    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请您输入存钱金额：");
        double money = sc.nextDouble();

        loginAccount.setMoney(money + loginAccount.getMoney());
        System.out.println("恭喜您，您存钱：" + money + "成功，存钱后余额是：" + loginAccount.getMoney());
    }

    // 展示当前登录的账户信息
    private void showLoginAccount(){
        System.out.println("===当前您的账户信息：===");
        System.out.println("卡号：" + loginAccount.getCardId());
        System.out.println("户主：" + loginAccount.getUserName());
        System.out.println("性别：" + loginAccount.getSex());
        System.out.println("余额：" + loginAccount.getMoney());
        System.out.println("每次取款限额" + loginAccount.getLimit());
    }
}
