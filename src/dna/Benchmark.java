package dna;

import java.util.List;
import java.util.function.BiFunction;

import dna.problems.P1Small;
import dna.problems.P2Large;
import dna.problems.Problem;

public class Benchmark {

	public static Result sequential(Problem p, Integer ncores) {

		int[] results = new int[p.getPatterns().size()]; 
		for (int i = 0; i < results.length; i++) {
			results[i]=0;
		}

		List<String> patterns = p.getPatterns();
		char[] dna =  p.getSearchSequence();

		for (int i = 0; i < dna.length; i++) {
			for (int j = 0; j < patterns.size(); j++) {
				if(isIn(dna, i, patterns.get(j))) {
					results[j]+=1;
				}
			}
		}

		return new Result(results);
	}

	public static Result parallel(Problem p, Integer ncores) {
		int[] results = new int[p.getPatterns().size()]; 
		for (int i = 0; i < results.length; i++) {
			results[i]=0;
		}
		List<String> patterns = p.getPatterns();
		char[] dna =  p.getSearchSequence();

		Thread[] threadLst= new Thread [Runtime.getRuntime().availableProcessors()];
		for (int i = 0; i < threadLst.length; i++) {
			final int index= i;

			threadLst[i] = new Thread(()->{

				int start= index *(dna.length/threadLst.length);
				int end=((index+1<threadLst.length)?
						(index+1)*(dna.length/threadLst.length): dna.length);
				for (int l = start; l < end; l++) {
					for (int j = 0; j < patterns.size(); j++) {
						synchronized(results) {
							if(isIn(dna, l, patterns.get(j))) {
								results[j]+=1;
							}
						}
					}
				}

			});
			threadLst[i].start();
		}
		for (Thread t : threadLst) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Thread " + t + " was interrupted");
			}
		}

		return new Result(results);
	}



	public static boolean isIn(char[] arr, int start, String pattern) {
		if ( (arr.length - start) < pattern.length()) return false;
		for (int i=0; i < pattern.length(); i++) {
			if (arr[start + i] != pattern.charAt(i)) return false;
		}
		return true;
	}

	public static void bench(Problem p, BiFunction<Problem, Integer, Result> f, String name) {

		int maxCores = Runtime.getRuntime().availableProcessors();
		for (int ncores=1; ncores<=maxCores; ncores *= 2) {

			for (int i=0; i< 30; i++) {
				long tseq = System.nanoTime();
				Result r = f.apply(p, ncores);
				tseq = System.nanoTime() - tseq;

				if (!r.compare(p.getSolution())) {
					System.out.println("Wrong result for " + name + ".");
					System.exit(1);
				}
				System.out.println(ncores + ";" + name + ";" + tseq);
			}
		}
	}

	public static void main(String[] args) {
		Problem p = (Runtime.getRuntime().availableProcessors() == 64) ? new P2Large() : new P1Small();
		Benchmark.bench(p, Benchmark::sequential, "seq");
		Benchmark.bench(p, Benchmark::parallel, "par");
	}
}
