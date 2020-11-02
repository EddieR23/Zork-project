package com.company;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Zork {
    public static PrintStream so = System.out;
    Scanner  source = new Scanner(System.in);
    public String input = "";

    //ArrayLists to store objects of the game
    //public HashMap<String, Room> Rooms = new HashMap<String, Room>();
    public LinkedList<String> Items = new LinkedList<String>();
    public HashMap<String, Container> Containers = new HashMap<String, Container>();
    public HashMap<String, Monster> Monsters = new HashMap<String, Monster>();
    public LinkedList<String> Backpack = new LinkedList<String>();
    public String curRoom;
    public File file;

    public Zork() throws FileNotFoundException {
        //file = new File("Sample.txt");

        if(!file.canRead()) {
            so.println("Error opening file...");
            return;
        }
        else {
            Scanner read = new Scanner(new File("/This PC/Downloads/Zork.txt"));

            while(read.hasNextLine()) {
                //if its a room
                if(read.nextLine().startsWith("room")) {

                }
                //if its an item
                else if(read.nextLine().startsWith("item:")){
                    Item tempItem = new Item();

                    String nameS = read.nextLine();
                    String[] name = nameS.split(" ", 2);
                    if(name.length > 0)
                        tempItem.name = name;
                    else if (read.nextLine().startsWith("command:"))
                        if(source.nextLine().equals("take water bottle")) {
                            Backpack.add(tempItem.name);
                            so.println("water bottle added to backpack\n");
                        }
                        else if (source.nextLine().equals("take bag of food"))  {
                            Backpack.add(tempItem.name);
                            so.println("bag of food added to backpack\n");
                        }
                        else if (source.nextLine().equals("take knife"))  {
                            Backpack.add(tempItem.name);
                            so.println("knife added to backpack\n");
                        }
                        else if (source.nextLine().equals("take all"))  {
                            Backpack.add(tempItem.name);
                            Backpack.add(tempItem.name);
                            Backpack.add(tempItem.name);
                            so.println("bag of food added to backpack\n");
                        }



                }

            }
        }

    }


    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//        String command;
//        while(!false) {
//            so.println("Enter a command: ");
//            command = input.nextLine();
//
//            String[] parts = command.split(" ",2);
//            for(int i =0; i < parts.length; i++) {
//                parts[i] = parts[i].replaceAll(" ","");
//            }
//
//            for (int i = 0; i < parts.length; i++)
//                if (parts[0] == "Look") {
//                    so.println("\nYou are here");
//                } else
//                    so.print("You don't know hou to do that");
//                    so.println();
//        }
        Maze maze;
        MazeGame game = new MazeGame();
        StandardMazeBuilder builder = new StandardMazeBuilder();
        game.CreateMaze(builder);
        maze = builder.GetMaze();
        so.println("my maze is" + maze);
    }
}
enum Direction {North, South, East, West};
abstract class MapSite { abstract void Enter(); }

class Room extends MapSite {
   // Direction direction;

    Room(int roomNo) {
        this.roomNumber = roomNo;
    }
    public MapSite GetSide(Direction direction) {
        if(direction.equals(Direction.North))
            return sides[0];
        else if (direction.equals(Direction.South))
            return sides[1];
        else if (direction.equals(Direction.East))
            return sides[2];
        else if (direction.equals(Direction.West))
            return sides[3];
        else
            return null;

    }
    public void SetSide(Direction direction, MapSite ms) {
        if(direction.equals(Direction.North))
            sides[0] = ms;
        else if (direction.equals(Direction.South))
            sides[1] = ms;
        else if (direction.equals(Direction.East))
            sides[2] = ms;
        else if (direction.equals(Direction.West))
            sides[3] = ms;
    }
   public void Enter() {
   }

    private MapSite[] sides = new MapSite[4];
    private int roomNumber;
}
class Wall extends MapSite {
    Wall() {}
    public void Enter() {}
}
class Door extends MapSite {
    Door(Room r1, Room r2) {
        this.room1 = r1;
        this.room2 = r2;
    }
    public void Enter() {
        this.isOpen = true;
    }

    public Room OtherSideFrom(Room room) {
        if (room.equals(room1))
            return room2;
        else
            return room1;
    }

    private Room room1;
    private Room room2;
    private boolean isOpen;
}
class Maze {
    private Room[] rooms = new Room[10];
    Maze() {}
    public void AddRoom(Room r) {
        for(int i = 0; i < rooms.length; i++)
            if(rooms[i] == null)
                rooms[i] = r;
    }
    public Room RoomNum(int num) {
        return rooms[num];
    }

}

class MazeGame {
    public Maze CreateMaze(MazeBuilder builder) {


        builder.BuildMaze();
        builder.BuildRoom(1);
        builder.BuildRoom(2);
        builder.BuildDoor(1,2);

        return builder.GetMaze();
    }
//    public Maze CreateComplexMaze(MazeBuilder builder) {
//        for(int i = 0; i <= 10; i++)
//            builder.BuildRoom(i);
//
//        return builder.GetMaze();
//    }
}

interface MazeBuilder{
    void BuildMaze();
    void BuildRoom(int room);
    void BuildDoor(int roomFrom, int roomTo);
    Maze GetMaze();

   // protected MazeBuilder();
}

class StandardMazeBuilder implements MazeBuilder {
    Maze currentMaze;
    private Direction CommonWall(Room room1, Room room2) {
        for(Direction i : Direction.values()){
            if(room1.GetSide(i).equals(room2.GetSide(Direction.North)))
                return i;
            if(room1.GetSide(i).equals(room2.GetSide(Direction.South)))
                return i;
            if(room1.GetSide(i).equals(room2.GetSide(Direction.East)))
                return i;
            if(room1.GetSide(i).equals(room2.GetSide(Direction.West)))
                return i;
        }
        return null;
    }

    public StandardMazeBuilder() {currentMaze = null;}
    @Override
    public void BuildMaze() {currentMaze = new Maze(); }
    @Override
    public void BuildRoom(int room) {
        if(currentMaze.RoomNum(room) == null) {
            Room rm = new Room(room);
            currentMaze.AddRoom(rm);

            rm.SetSide(Direction.North, new Wall());
            rm.SetSide(Direction.South, new Wall());
            rm.SetSide(Direction.East, new Wall());
            rm.SetSide(Direction.West, new Wall());
        }
    }
    @Override
    public void BuildDoor(int roomFrom, int roomTo) {
        Room r1 = currentMaze.RoomNum(roomFrom);
        Room r2 = currentMaze.RoomNum(roomTo);
        Door d = new Door(r1, r2);

        r1.SetSide(CommonWall(r1,r2), d);
        r2.SetSide(CommonWall(r2,r1), d);
    }
    @Override
    public Maze GetMaze() { return currentMaze; }



}
//Everything inherits from this class
class Object {
    public String status;
    public Object() {}
}


//Items
class Item extends Object {
    public String name;
    public String description;
    public String writing;
    public ArrayList<String> turnOnPrint = new ArrayList<String>();
    public ArrayList<String> turnOnAction = new ArrayList<String>();
    public Item() { }
}

//Container
class Container extends Object {
    public String name;
    public HashMap<String, String> item = new HashMap<String, String>();
    public String description;
    public ArrayList<String> accept = new ArrayList<String>();
    boolean isOpen;
    public Container() {}
}

//Monster
class Monster extends Object {
    public String name;
    public String description;
    public ArrayList<String> print = new ArrayList<String>();
    public ArrayList<String> action = new ArrayList<String>();

    public Monster() { }
    public boolean attack(Zork zork, String weapon){
        for (int i = 0; i < conditions.size(); i++) {
            if(!conditions.get(i).evaluate(zork))
                return false;
        }
        return true;
    }
}




