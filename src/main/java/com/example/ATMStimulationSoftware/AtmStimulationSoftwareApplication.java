package com.example.ATMStimulationSoftware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.*;
import java.util.*;


@SpringBootApplication
public class AtmStimulationSoftwareApplication implements CommandLineRunner
{
	@Autowired
	UserRepository repository;

	@Autowired
	TransactionsRepository txn;

	public static void main(String[] args)
	{
		SpringApplication.run(AtmStimulationSoftwareApplication.class, args);
		//Controller.main(args);
	}

	@Override
	public void run(String []args)
	{
		Scanner sc=new Scanner(System.in);//for input support

		DateFormat df=new SimpleDateFormat("dd/MM/yy HH:mm:ss");//Date-time format
		try
		{
			System.out.println("\n***************************************");
			System.out.println("    WELCOME TO STATE BANK OF INDIA\n");
			while(true)
			{
				System.out.println("________________________________________");
				System.out.println("For New User >>Press 1 -Open new Account");
				System.out.println("Existing User>>Press 2 -go to Login Page\n");
				System.out.println("________________________________________");

				int acc,c,found=0,pin,op,del,new_pin,rec_acc;
				double amt,rec_amt;
				User active=null;//stores the active user object
				c=sc.nextInt();
				switch(c)
				{
					case 1:System.out.println("    >> SIGN UP <<");
						System.out.println("(Note: Don't use Space in Name)");
						System.out.print("Enter First Name: ");
						String new_name_f=sc.next();
						System.out.print("Enter Last Name: ");
						String new_name_l=sc.next();

						int random_pin=(int)(Math.random()*(9999-1000+1)+1000);

						User u=new User();
						u.setName(new_name_f+" "+new_name_l);
						u.setBalance(0.0);
						u.setPin(random_pin);
						repository.save(u);

						System.out.println("\n>>New Account Created Successfully<<");
						System.out.println("New Acc. no.="+u.getAccount_id());
						System.out.println("New Pin="+random_pin);
						System.out.println("Login to access your new Account & Change your Pin");
						break;

					case 2:System.out.println("    >>LOGIN<<");
						System.out.print("Enter Account Number:");
						acc=sc.nextInt();
						User u1=repository.findById(acc).orElse(null);
						if(u1!=null)
						{
							found = 1;
							System.out.print("Enter Pin:");
							pin = sc.nextInt();
							if(u1.getPin()!=pin)
							{
								System.out.println("\n      !!WRONG PIN!!");
								System.out.println("!!TWO MORE ATTEMPTS LEFT!!");
								System.out.print("\nEnter Pin:");
								pin = sc.nextInt();
							}
							if(u1.getPin()!=pin)
							{
								System.out.println("      !!WRONG PIN!!");
								System.out.println("!!LAST ATTEMPT LEFT!!");
								System.out.print("\nEnter Pin:");
								pin = sc.nextInt();
							}
							if (u1.getPin() == pin)
							{
								active = u1;//active user
								System.out.println("\n<Successfully Logged in at " + df.format(new Date()) + ">\n");
								System.out.print("\n      ** Welcome " + active.getName() + " **");
								found = 2;
							}
						}
						if(found==0)
						{
							System.out.println("\n!!INVALID ACCOUNT NUMBER!!");
							System.out.println("!!PLEASE TRY AGAIN LATER!!");
							System.out.println("\n<Session ended at "+df.format(new Date())+">\n");
							System.exit(0);
						}
						if(found==1)
						{
							System.out.println("\n!!INVALID PIN!!");
							System.out.println("!!PLEASE TRY AGAIN LATER!!");
							System.out.println("\n<Session ended at "+df.format(new Date())+">\n");
							System.exit(0);
						}
						while (true)
						{
							System.out.println("\n__________________");
							System.out.println("SELECT OPERATION:");
							System.out.println("1-Update Pin");
							System.out.println("2-Balance Enquiry");
							System.out.println("3-Withdraw Money");
							System.out.println("4-Deposit Money");
							System.out.println("5-NEFT Fund Transfer");
							System.out.println("6-Delete Account");
							System.out.println("7-Logout");
							System.out.println("\n__________________");
							int ch=sc.nextInt();
							switch (ch)
							{
								case 1:System.out.print("Enter Old Pin:");
									op=sc.nextInt();
									if(op==active.getPin())
									{
										System.out.print("Enter New Pin:");
										new_pin=sc.nextInt();
										if(new_pin>=1000 && new_pin<=9999)
										{
											active.setPin(new_pin);
											repository.save(active);
											System.out.println(">> PIN UPDATED SUCCESSFULLY <<");
										}
										else
										{
											System.out.println("\n!!PIN MUST BE OF 4 DIGITS!!");
											System.out.println(">> PIN NOT UPDATED, TRY AGAIN <<");
										}
									}
									else
									{
										System.out.println("\n	!!INVALID PIN!! ");
									}
									break;

								case 2:System.out.println("AVAILABLE BALANCE = Rs."+active.getBalance());
									break;

								case 3:System.out.print("\nEnter Amount to be Withdrawn: Rs.");
									amt=sc.nextDouble();
									if(amt>active.getBalance())
										System.out.println("\n !!INSUFFICIENT BALANCE!!");
									else
									{
										active.setBalance(active.getBalance()-amt);
										repository.save(active);
										System.out.println(">> WITHDRAWL SUCCESSFUL<<");
										System.out.println("AVAILABLE BALANCE = Rs."+active.getBalance());
									}
									break;

								case 4:System.out.print("\nEnter Amount to be Deposited: Rs.");
									amt=sc.nextDouble();
									if(amt>1000000)
									{
										System.out.println("\n!!YOUR AMOUNT EXCEEDS LIMIT!!");
										System.out.println("Maximum depositing amount at once = Rs. 1000000");
									}
									else
									{
										active.setBalance(active.getBalance()+amt);
										repository.save(active);
										System.out.println(">> DEPOSIT SUCCESSFUL<<");
										System.out.println("AVAILABLE BALANCE = Rs."+active.getBalance());
									}
									break;

								case 5:System.out.print("\nEnter Receiver's Account Number:");
									   rec_acc=sc.nextInt();
									   User receiver=repository.findById(rec_acc).orElse(null);
									   if(receiver!=null && receiver.getAccount_id()!=active.getAccount_id())
									   {
									   	System.out.print("Enter Amount:");
										rec_amt=sc.nextDouble();
										if(rec_amt<=active.getBalance())
										{
											active.setBalance(active.getBalance()-rec_amt);
											receiver.setBalance(receiver.getBalance()+rec_amt);
											repository.save(active);
											repository.save(receiver);
											Transactions t=new Transactions();
											t.setSender(active.getAccount_id());
											t.setReceiver(receiver.getAccount_id());
											t.setAmount(rec_amt);
											t.setDate(new Date());
											txn.save(t);
											System.out.println("\n>> Rs."+rec_amt+" Transferred Successfully to "+receiver.getName()+" <<");
											System.out.println("\nAVAILABLE BALANCE = Rs."+active.getBalance());
										}
										else
										{
											   System.out.println("\n	!!INSUFFICIENT BALANCE!! ");
											   System.out.println("\n	!!FUND TRANSFER FAILED!! ");
										}
									   }
								       else
									   {
										   System.out.println("\n	!!INVALID RECEIVER'S ACCOUNT NUMBER!! ");
										   System.out.println("\n	!!FUND TRANSFER FAILED!! ");
									   }
								       break;

								case 6:System.out.println("Are you sure, you want to delete your account?");
									   System.out.println("1-Yes\n2-No");
									   del=sc.nextInt();
									   switch (del)
									   {
										   case 1:repository.delete(active);
										   		System.out.println(">> ACCOUNT DELETED SUCCESSFULLY<<");
											    System.out.println("\n<Session ended at "+df.format(new Date())+">\n");
											    System.exit(0);

										   case 2:System.out.println(">> ACCOUNT NOT DELETED<<");
										   	    break;
										   default:System.out.println("\n!!WRONG CHOICE!!\nPress between 1 to 2");
									   }
									   break;

								case 7:System.out.println("\nThanks for using SBI, visit again :)");
									System.out.println("\n<Session ended at "+df.format(new Date())+">\n");
									System.exit(0);
								default:System.out.println("\n!!WRONG CHOICE!!\nPress between 1 to 5");
							}//end of inner switch
						}//end of inner while
					default:System.out.println("\n!!WRONG CHOICE!!\nPress between 1 to 2");
				}//end of outer switch
			}//end of outer while
		}
		catch (Exception e)
		{
			System.out.println("\n     !!WARNING!!\nPlease use correct input format");
			System.out.println("\n<Session Expired at "+df.format(new Date())+">\n");
			System.exit(0);
		}//end of catch block
	}

}
