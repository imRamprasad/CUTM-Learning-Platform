# ================================================================================
# GitHub Branch Protection Rules Configuration
# ================================================================================
# Apply these rules through GitHub Settings > Branches > Branch Protection Rules
# Repository: CUTM-Learning-Platform

# ================================================================================
# For: main (Production Branch)
# ================================================================================
Main branch protections:
  - Rule name: Production Protection
  - Require pull request reviews before merging: YES
    - Require approvals: 2
    - Require review from code owners: YES
    - Require approval of the most recent reviewable push: YES
  - Require status checks to pass before merging:
    - Build and Test (GitHub Actions)
    - Code quality check
  - Require branches to be up to date before merging: YES
  - Require conversation resolution before merging: YES
  - Require signed commits: NO (Optional, set to YES for higher security)
  - Require linear history: NO
  - Allow force pushes: NO
  - Allow deletions: NO

# ================================================================================
# For: develop (Integration Branch)
# ================================================================================
Develop branch protections:
  - Rule name: Develop Protection
  - Require pull request reviews before merging: YES
    - Require approvals: 1
    - Require review from code owners: YES
    - Require approval of the most recent reviewable push: YES
  - Require status checks to pass before merging:
    - Build and Test (GitHub Actions)
  - Require branches to be up to date before merging: NO (Allow fast forward merge)
  - Require conversation resolution before merging: YES
  - Require signed commits: NO
  - Require linear history: NO
  - Allow force pushes: NO (except for admins)
  - Allow deletions: NO

# ================================================================================
# Suggested Settings
# ================================================================================

# admin/ branches (Hotfixes)
admin-protection:
  - Require approvals: 1
  - Require code owner review: YES
  - Bypass branch protection: Tech lead only

# Create the rules in GitHub:
# 1. Go to: https://github.com/<org>/CUTM-Learning-Platform/settings/branches
# 2. Add rule for "main" with the above settings
# 3. Add rule for "develop" with the above settings
# 4. Add rule for "feature/*" (optional - less strict)
# 5. Add rule for "admin/*" (strict - for hotfixes)

