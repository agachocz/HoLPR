public class Decision extends Message{

	private Authority authority;
	private String quote = "Cytat";
	private String title = "Tytu³";
	private String text = "Tekst";
	private String optionText1 = "Za";
	private String optionText2 = "Przeciw";
	
	//Skutki podjêcia decyzji; 0 - money, 1 - votes, 2 - sausage
	private int[] option1;
	private int[] option2;
	
	public Decision(String title, String text, Authority aut, String quote, int[] option1, int[] option2)
	{
		super(text);
		this.title = title;
		this.quote = quote;
		this.authority = aut;
		this.option1 = option1;
		this.option2 = option2;
		this.optionText1 = "Za";
		this.optionText2 = "Przeciw";

	}
	
	public void action(World world, boolean choice)
	{
		if(choice){
			world.getCurParty().addMoney(option1[0]);
			world.getCurParty().addVotes(option1[1]);
			world.getCurParty().addSausage(option1[2]);
		}
		else {
			world.getCurParty().addMoney(option2[0]);
			world.getCurParty().addVotes(option2[1]);
			world.getCurParty().addSausage(option2[2]);
			}
	}

	public String getQuote(){return quote;}
	public String getTitle(){return title;}
	public Authority getAuthority(){return authority;}
	public String getOptionText1(){return optionText1;}
	public String getOptionText2(){return optionText2;}
	
	public int getMoney(boolean b){
		if(!b) return option1[0];
		else return option2[0];
	}
	
	public int getVotes(boolean b){
		if(!b) return option1[1];
		else return option2[1];
	}
	
	public int getSausage(boolean b){
		if(!b) return option1[2];
		else return option2[2];
	}
	
}
