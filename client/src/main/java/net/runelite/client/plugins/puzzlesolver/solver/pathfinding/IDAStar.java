/*
 * Copyright (c) 2018, Lotto <https://github.com/devLotto>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.puzzlesolver.solver.pathfinding;

import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.Heuristic;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the IDA* algorithm.
 *
 * https://en.wikipedia.org/wiki/Iterative_deepening_A*
 */
public class IDAStar extends Pathfinder
{
	public IDAStar(Heuristic heuristic)
	{
		super(heuristic);
	}

	@Override
	public List<PuzzleState> computePath(PuzzleState root)
	{
		PuzzleState goalNode = path(root);

		List<PuzzleState> path = new ArrayList<>();

		PuzzleState parent = goalNode;
		while (parent != null)
		{
			path.add(0, parent);
			parent = parent.getParent();
		}

		return path;
	}

	private PuzzleState path(PuzzleState root)
	{
		int bound = root.getHeuristicValue(getHeuristic());

		while (true)
		{
			PuzzleState t = search(root, 0, bound);

			if (t != null)
			{
				return t;
			}

			bound += 1;
		}
	}

	private PuzzleState search(PuzzleState node, int g, int bound)
	{
		int h = node.getHeuristicValue(getHeuristic());
		int f = g + h;

		if (f > bound)
		{
			return null;
		}

		if (h == 0)
		{
			return node;
		}

		for (PuzzleState successor : node.computeMoves())
		{
			PuzzleState t = search(successor, g + 1, bound);

			if (t != null)
			{
				return t;
			}
		}

		return null;
	}
}
