import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SearchAlgorithm
{
    //This is the code for backtracking using degree heuristic
    public Schedule MaxBackTracking(SchedulingProblem problem, long deadline)
    {
        Schedule solution = problem.getEmptySchedule();
        //Here i make 2 arrays that ill use to keep track of the constrains
        int Mconstrains[] = new int[problem.courses.size()];
        int counstrains[] = new int[problem.courses.size()];
        for (int i = 0; i < problem.courses.size(); i++)
        {
            counstrains[i]=i;
            int count=0;
            for (int j = 0; j < problem.NUM_TIME_SLOTS; j++)
            {
                if(problem.courses.get(i).timeSlotValues[j] == 0)
                {
                    count++;
                }
            }
        }
        //Here i use bubble sort to find the classes with the biggest constrain and store their index in "constrains"
        int n = Mconstrains.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (Mconstrains[j] < Mconstrains[j+1])
                {
                    int temp = Mconstrains[j];
                    Mconstrains[j] = Mconstrains[j+1];
                    Mconstrains[j+1] = temp;
                    int temp2 = counstrains[j];
                    counstrains[j] = counstrains[j+1];
                    counstrains[j+1] = temp2;
                }
        return MaxRecBackTracking(solution,problem,0,counstrains);
    }
    public Schedule MaxRecBackTracking(Schedule solution,SchedulingProblem problem,int size,int Marray[])
    {
        //If i assign every class then i stop the recursion
        if (size >= problem.courses.size())
        {
            return solution;
        }
        Course var = problem.courses.get(Marray[size]);
        //Iterate every time slot
        for (int i = 0; i < var.timeSlotValues.length; i++)
        {
            if (var.timeSlotValues[i] > 0)
            {
                for (int k = 0; k < problem.rooms.size(); k++)
                {
                    if (solution.schedule[k][i] < 0)
                    {
                        //if the schedule is open then put the class
                        solution.schedule[k][i] = size;
                        //start the recursion with another class
                        Schedule Result = MaxRecBackTracking(solution,problem, size += 1,Marray);
                        if(Result !=  null)
                        {
                            return solution;
                        }
                        solution.schedule[k][i] = -1;
                    }
                }
            }
        }
        return null;
    }
    //This is the code for backtracking using Minimum remaining values
    public Schedule MinBackTracking(SchedulingProblem problem, long deadline)
    {
        Schedule solution = problem.getEmptySchedule();
        //Here i make 2 arrays that ill use to keep track of the constrains
        int Mconstrains[] = new int[problem.courses.size()];
        int counstrains[] = new int[problem.courses.size()];
        for (int i = 0; i < problem.courses.size(); i++)
        {
            counstrains[i]=i;
            int count=0;
            for (int j = 0; j < problem.NUM_TIME_SLOTS; j++)
            {
                if(problem.courses.get(i).timeSlotValues[j] == 0)
                {
                    count++;
                }
            }
        }
        //Here i use bubble sort to find the classes with the smallest constrain and store their index in "constrains"
        int n = Mconstrains.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (Mconstrains[j] > Mconstrains[j+1])
                {
                    int temp = Mconstrains[j];
                    Mconstrains[j] = Mconstrains[j+1];
                    Mconstrains[j+1] = temp;
                    int temp2 = counstrains[j];
                    counstrains[j] = counstrains[j+1];
                    counstrains[j+1] = temp2;
                }
        return MinRecBackTracking(solution,problem,0,counstrains);
    }
    public Schedule MinRecBackTracking(Schedule solution,SchedulingProblem problem,int size,int Marray[])
    {
        //If i assign every class then i stop the recursion
        if (size >= problem.courses.size())
        {
            return solution;
        }
        Course var = problem.courses.get(Marray[size]);
        //Iterate every time slot
        for (int i = 0; i < var.timeSlotValues.length; i++)
        {
            if (var.timeSlotValues[i] > 0)
            {
                for (int k = 0; k < problem.rooms.size(); k++)
                {
                    if (solution.schedule[k][i] < 0)
                    {
                        solution.schedule[k][i] = size;
                        Schedule Result = MinRecBackTracking(solution,problem, size += 1,Marray);
                        if(Result !=  null)
                        {
                            return solution;
                        }
                        solution.schedule[k][i] = -1;
                    }
                }
            }
        }
        return null;
    }
    public Schedule BackTracking(SchedulingProblem problem, long deadline)
    {
        Schedule solution = problem.getEmptySchedule();
        return RecBackTracking(solution,problem,0);
    }
    public Schedule RecBackTracking(Schedule solution,SchedulingProblem problem,int size)
    {
        if (size >= problem.courses.size())
        {
            return solution;
        }
        Course var = problem.courses.get(size);
        for (int i = 0; i < var.timeSlotValues.length; i++)
        {
            if (var.timeSlotValues[i] > 0)
            {
                for (int k = 0; k < problem.rooms.size(); k++)
                {
                    //if the schedule is open then put the class
                    if (solution.schedule[k][i] < 0)
                    {
                        solution.schedule[k][i] = size;
                        //start the recursion with another class
                        Schedule Result = RecBackTracking(solution,problem, size += 1);
                        if(Result !=  null)
                        {
                            return solution;
                        }
                        solution.schedule[k][i] = -1;
                    }

                }
            }
        }
        return null;
    }

    //Simulated annealing strategy
    public Schedule Sim_annealing(SchedulingProblem problem, long deadline, int Temp)
    {
        // get an empty solution to start from
        Schedule Best_solution = problem.getEmptySchedule();
        double Best_Score = 0;
        //I create two array list to keep track of current values
        ArrayList<Integer> CurrSlot = new ArrayList<>();
        ArrayList<Integer> CurrRoom = new ArrayList<>();
        //populate the values
        for (int i = 0; i < problem.NUM_TIME_SLOTS ; i++)
        {
            CurrSlot.add(i);
            CurrRoom.add(i);
        }
        //I shuffle the Arraylist to keep the first value random
        Collections.shuffle(CurrSlot);
        Collections.shuffle(CurrRoom);
        while (Temp > 0)
        {
            Schedule Curr_solution = problem.getEmptySchedule();
            ArrayList<Integer> RandRoom = new ArrayList<>(CurrRoom);
            ArrayList<Integer> RandSlot = new ArrayList<>(CurrSlot);
            //I use this to move hte current node
            for (int i = 0; i < problem.NUM_TIME_SLOTS/3; i++)
            {
                Collections.swap(RandSlot,(int)(Math.random()*problem.NUM_TIME_SLOTS),(int)(Math.random()*problem.NUM_TIME_SLOTS));
            }
            for (int i = 0; i < problem.rooms.size()/3; i++)
            {
                Collections.swap(RandRoom,(int)(Math.random()*problem.rooms.size()),(int)(Math.random()*problem.rooms.size()));
            }
            //Start iteration
            for (int i = 0; i < problem.courses.size(); i++)
            {
                Course c = problem.courses.get(i);
                boolean scheduled = false;
                for (int j = 0; j < c.timeSlotValues.length; j++)
                {
                    if (scheduled) break;
                    if (c.timeSlotValues[CurrSlot.get(j)] > 0)
                    {
                        for (int k = 0; k < problem.rooms.size(); k++)
                        {
                            if (Curr_solution.schedule[RandRoom.get(k)][RandSlot.get(j)] < 0)
                            {
                                Curr_solution.schedule[RandRoom.get(k)][RandSlot.get(j)] = i;
                                scheduled = true;
                                break;
                            }
                        }
                    }
                }
            }
            //If the current value is better change very value tot he current
            Double Sum = problem.evaluateSchedule(Curr_solution) - Best_Score;
            if( Sum > 0)
            {
                Best_Score = problem.evaluateSchedule(Curr_solution);
                Best_solution = Curr_solution;
                CurrRoom = RandRoom;
                CurrSlot = RandSlot;

            }
            //using the temperature check with randomness if it pass
            else if(Math.exp(Sum/Temp) > Math.random())
            {
                CurrRoom = RandRoom;
                CurrSlot = RandSlot;
            }
            Temp --;

        }
        return Best_solution;
    }
    public Schedule naiveBaseline(SchedulingProblem problem, long deadline)
    {
        // get an empty solution to start from
        Schedule solution = problem.getEmptySchedule();
        for (int i = 0; i < problem.courses.size(); i++)
        {
            Course c = problem.courses.get(i);
            boolean scheduled = false;
            for (int j = 0; j < c.timeSlotValues.length; j++)
            {
                if (scheduled) break;
                if (c.timeSlotValues[j] > 0)
                {
                    for (int k = 0; k < problem.rooms.size(); k++)
                    {
                        if (solution.schedule[k][j] < 0)
                        {
                            solution.schedule[k][j] = i;
                            scheduled = true;
                            break;
                        }
                    }
                }
            }
        }
        return solution;
    }

}
